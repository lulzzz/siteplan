/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JSimpleList.java
 *
 * Created on 19-Mar-2011, 00:26:03
 */
package camp.anim;

import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;

/**
 *
 * @author twak
 */
public abstract class JSimpleList<E> extends javax.swing.JPanel
{
    List<E> stuff;

    /** Creates new form JSimpleList */
    public JSimpleList()
    {
        initComponents();
    }

    public JSimpleList( String title, List<E> stuff )
    {
        this.stuff = stuff;
        initComponents();

        titleLabel.setText( title );

        jList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        initList();

        try
        {
            addButton.setVisible( getClass().getMethod( "create" ).getDeclaringClass() != JSimpleList.class );
            removeButton.setVisible( getClass().getMethod( "onDelete" ).getDeclaringClass() != JSimpleList.class );
//            editButton.setVisible( getClass().getMethod( "onEdit" ).getDeclaringClass() != JSimpleList.class );
        }
        catch (NoSuchMethodException e)
        {} // don't care
        catch ( Throwable ex )
        {
            ex.printStackTrace();
        }

    }

    void initList()
    {
        DefaultListModel dlm = new DefaultListModel();
        Object selected = jList.getSelectedValue();

        for ( E e : stuff )
            dlm.addElement( e );

        jList.setModel( dlm );

        if ( selected != null )
            jList.setSelectedValue( selected, true );
    }

    /**
     * Override the following methods to enable their buttons
     */
    public E create() { return null; }

    public void onCreate(){}

    public void onDelete(){}

    public void onEdit( E e ){}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        addButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList = new javax.swing.JList();
        titleLabel = new javax.swing.JLabel();

        jPanel1.setLayout(new java.awt.GridLayout(0, 1));

        addButton.setText("+");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });
        jPanel1.add(addButton);

        removeButton.setText("-");
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });
        jPanel1.add(removeButton);

        editButton.setText("e");
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });
        jPanel1.add(editButton);

        jList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jList);

        titleLabel.setText("title");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(titleLabel)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 367, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_addButtonActionPerformed
    {//GEN-HEADEREND:event_addButtonActionPerformed
        stuff.add( create() );
        initList();
        onCreate();
    }//GEN-LAST:event_addButtonActionPerformed

    private void jListValueChanged(javax.swing.event.ListSelectionEvent evt)//GEN-FIRST:event_jListValueChanged
    {//GEN-HEADEREND:event_jListValueChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jListValueChanged

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_editButtonActionPerformed
    {//GEN-HEADEREND:event_editButtonActionPerformed
        E selected = (E) jList.getSelectedValue();
        if ( selected != null )
            onEdit( selected );
    }//GEN-LAST:event_editButtonActionPerformed

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_removeButtonActionPerformed
    {//GEN-HEADEREND:event_removeButtonActionPerformed
        E selected = (E) jList.getSelectedValue();
        if ( selected != null )
        {
            stuff.remove( selected );
            initList();
            onDelete();
        }
    }//GEN-LAST:event_removeButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton editButton;
    private javax.swing.JList jList;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton removeButton;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
}
