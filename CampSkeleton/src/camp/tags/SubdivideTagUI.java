/*
 * SubdivideUI.java
 *
 * Created on 17-Nov-2010, 20:23:52
 */

package camp.tags;

import camp.anchors.JProfileProfileMap;
import campskeleton.CampSkeleton;
import campskeleton.Plan;
import java.util.ArrayList;
import utils.WeakListener.Changed;

/**
 *
 * @author twak
 */
public class SubdivideTagUI extends javax.swing.JPanel {

    public SubdivideTag tag;
    Plan plan;

    /** Creates new form SubdivideUI */
    public SubdivideTagUI() {
        initComponents();
    }

    public SubdivideTagUI(Changed rebuildFeatureList, Plan plan, SubdivideTag tag)
    {
        this.tag = tag;
        this.plan = plan;
        
        initComponents();

        outputArea.setSelected(tag.enabled);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        outputArea = new javax.swing.JCheckBox();
        outputMap = new JStringProfileCheckMap(plan, tag.assignments, SubdivideTag.types);

        outputArea.setText("outputArea");
        outputArea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                outputAreaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(outputArea, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
            .addComponent(outputMap, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(outputArea)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(outputMap, javax.swing.GroupLayout.PREFERRED_SIZE, 239, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void outputAreaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_outputAreaActionPerformed
        tag.enabled = outputArea.isSelected();
        outputMap.setVisible(tag.enabled);
        CampSkeleton.instance.somethingChanged();
    }//GEN-LAST:event_outputAreaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox outputArea;
    private camp.tags.JStringProfileCheckMap outputMap;
    // End of variables declaration//GEN-END:variables

}
