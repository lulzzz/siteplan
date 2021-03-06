/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package camp.jme;

import com.jme.image.Texture;
import com.jme.image.Texture2D;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.renderer.TextureRenderer;
import com.jme.renderer.pass.Pass;
import com.jme.scene.Spatial;
import com.jme.scene.Spatial.TextureCombineMode;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.GLSLShaderObjectsState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;

/**
 * GLSL sketch effect pass.
 * - Render supplied source to a texture
 * - Render normals and depth to texture
 * - Apply sobel filter to find changes is normal and depth
 * - (Blur if wanted, slow)
 * - Overwrite or blend with first pass
 *
 * @author Rikard Herlitz (MrCoder)
 */
public class SketchOverRenderPass extends Pass {
    private static final long serialVersionUID = 1L;

    private TextureRenderer tRendererDepth;
	private Texture2D textureDepth;

	private Quad fullScreenQuad;

	private GLSLShaderObjectsState sobelShader;
	private GLSLShaderObjectsState normShader;

	private float normalMult = 10.5f;
	private float depthMult = 1250.0f;
	private float off = 0.0001f;
	private boolean supported = true;

	protected RenderState[] preStates = new RenderState[RenderState.StateType.values().length];

	/**
	 * Reset sketch parameters to default
	 */
	public void resetParameters() {
		normalMult = 22.5f;
		depthMult = 1850.0f;
		off = 0.0009f;
	}

	/**
	 * Release pbuffers in TextureRenderer's. Preferably called from user cleanup method.
	 */
	public void cleanup() {
		tRendererDepth.cleanup();
	}

	public boolean isSupported() {
		return supported;
	}

	public SketchOverRenderPass(Camera cam, int renderScale) {
//        try
        {
            //Test for glsl support
            if ( !GLSLShaderObjectsState.isSupported() )
            {
                supported = false;
                return;
            }
            DisplaySystem display = DisplaySystem.getDisplaySystem();
            resetParameters();
            //Create texture renderers and rendertextures for depth (only needed for some nvidia cards, like mine)
            tRendererDepth = display.createTextureRenderer( display.getWidth() / renderScale, display.getHeight() / renderScale, TextureRenderer.Target.Texture2D );
            tRendererDepth.setBackgroundColor( new ColorRGBA( 0.0f, 0.0f, 0.0f, 1.0f ) );
            tRendererDepth.setCamera( cam );
            textureDepth = new Texture2D();
            textureDepth.setWrap( Texture.WrapMode.Clamp );
            textureDepth.setMagnificationFilter( Texture.MagnificationFilter.Bilinear );
            tRendererDepth.setupTexture( textureDepth );
            //Create extract normals and depth shader
            normShader = display.getRenderer().createGLSLShaderObjectsState();

//            SketchOverRenderPass.class.getClassLoader().getResource("data/shader/pass/sketch_norm.vert")
            normShader.load(
                    SketchOverRenderPass.class.getClassLoader().getResource( Preview.PATH + "sketch_norm.vert" ),
                    SketchOverRenderPass.class.getClassLoader().getResource( Preview.PATH + "sketch_norm.frag" ) );

//            normShader.load( new FileInputStream( "shader/sketch_norm.vert" ), new FileInputStream( "shader/sketch_norm.frag" ) );
            normShader.setEnabled( true );
            normShader.setUniform( "nearClip", cam.getFrustumNear() );
            normShader.setUniform( "diffClip", cam.getFrustumFar() - cam.getFrustumNear() );
            //Create sobel shader
            sobelShader = display.getRenderer().createGLSLShaderObjectsState();


            sobelShader.load(
                    SketchOverRenderPass.class.getClassLoader().getResource( Preview.PATH + "sketch_sobel.vert" ),
                    SketchOverRenderPass.class.getClassLoader().getResource( Preview.PATH + "sketch_sobel.frag" ) );

//            sobelShader.load(  new FileInputStream(  "shader/sketch_sobel.vert" ),  new FileInputStream( "shader/sketch_sobel.frag" ) );

            sobelShader.setEnabled( true );
            //Create fullscreen quad
            fullScreenQuad = new Quad( "FullScreenQuad", display.getWidth(), display.getHeight() );
            fullScreenQuad.getLocalRotation().set( 0, 0, 0, 1 );
            fullScreenQuad.getLocalTranslation().set( display.getWidth() / 2, display.getHeight() / 2, 0 );
            fullScreenQuad.getLocalScale().set( 1, 1, 1 );
            fullScreenQuad.setRenderQueueMode( Renderer.QUEUE_ORTHO );
            fullScreenQuad.setCullHint( Spatial.CullHint.Never );
            fullScreenQuad.setTextureCombineMode( TextureCombineMode.Replace );
            fullScreenQuad.setLightCombineMode( Spatial.LightCombineMode.Off );
            TextureState ts = display.getRenderer().createTextureState();
            ts.setEnabled( true );
            fullScreenQuad.setRenderState( ts );
            BlendState as = display.getRenderer().createBlendState();
            as.setBlendEnabled( true );
            as.setSourceFunction( BlendState.SourceFunction.SourceAlpha );
            as.setEnabled( true );
            fullScreenQuad.setRenderState( as );
            fullScreenQuad.updateRenderState();
            fullScreenQuad.updateGeometricState( 0.0f, true );
            //setup shader for extracting normals and depth
            noTexture = display.getRenderer().createTextureState();
            noTexture.setEnabled( false );
            noLights = display.getRenderer().createLightState();
            noLights.setEnabled( false );
            noMaterials = display.getRenderer().createMaterialState();
            noMaterials.setEnabled( false );
        }
//        catch ( FileNotFoundException ex )
//        {
//            Logger.getLogger( SketchOverRenderPass.class.getName() ).log( Level.SEVERE, null, ex );
//        }
	}

	protected static TextureState noTexture;
	protected static LightState noLights;
	protected static MaterialState noMaterials;

    public void doRender( Renderer r ) {
        if ( spatials.size() != 1 )
            return;

        //Render scene to normals and depth
        saveEnforcedStates();
        context.enforceState( noTexture );
        context.enforceState( noLights );
        context.enforceState( noMaterials );
        context.enforceState( normShader );
        tRendererDepth.render( spatials.get( 0 ), textureDepth );
        replaceEnforcedStates();

        TextureState ts = (TextureState) fullScreenQuad.getRenderState( RenderState.StateType.Texture );

        //Apply sobel as final render
        sobelShader.clearUniforms();
        sobelShader.setUniform( "depth", 0 );
        sobelShader.setUniform( "normalMult", getNormalMult() );
        sobelShader.setUniform( "depthMult", getDepthMult() );
        sobelShader.setUniform( "off", getOff() );

        ts.setTexture( textureDepth, 0 );
        fullScreenQuad.setRenderState( sobelShader );
        fullScreenQuad.updateRenderState();
        r.draw( fullScreenQuad );
    }

	/**
     * saves any states enforced by the user for replacement at the end of the
     * pass.
     */
    protected void saveEnforcedStates() {
        for(int x = RenderState.StateType.values().length; --x >= 0;) {
            preStates[x] = context.enforcedStateList[x];
        }
    }

    /**
     * replaces any states enforced by the user at the end of the pass.
     */
    protected void replaceEnforcedStates() {
        for(int x = RenderState.StateType.values().length; --x >= 0;) {
            context.enforcedStateList[x] = preStates[x];
        }
    }

    public float getNormalMult() {
        return normalMult;
    }

    public void setNormalMult(float normalMult) {
        this.normalMult = normalMult;
    }

    public float getDepthMult() {
        return depthMult;
    }

    public void setDepthMult(float depthMult) {
        this.depthMult = depthMult;
    }

    public float getOff() {
        return off;
    }

    public void setOff(float off) {
        this.off = off;
    }
}