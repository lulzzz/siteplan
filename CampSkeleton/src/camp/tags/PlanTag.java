
package camp.tags;

import camp.jme.Preview;
import campskeleton.Plan;
import campskeleton.Profile;
import java.util.List;
import java.util.Set;
import javax.swing.JComponent;
import straightskeleton.Output;
import straightskeleton.Tag;
import straightskeleton.ui.Bar;
import utils.WeakListener.Changed;

/**
 *
 * @author twak
 */
public class PlanTag extends Tag
{
    String className;

    public PlanTag (String name)
    {
        super(name);
    }

    public JComponent getToolInterface(Changed rebuildFeatureList, Plan plan) {
        return null;
    }

    public static PlanTag createATag(String className) {
        try {
            PlanTag tag = (PlanTag) Class.forName(className).newInstance();
            tag.className = className;
            return tag;
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void postProcess(Output output, Preview preview, Object threadKey) {
        // override to add something to the output geometry
    }

    /**
     * @param vProfiles
     */
    public void addUsedProfiles( List<Profile> vProfiles )
    {
        // override me
    }

    public void update( int frame )
    {
    }

    public void addUsedBars(Set<Bar> out) {
        // override me
    }
}
