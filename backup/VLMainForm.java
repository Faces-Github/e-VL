package manifetproject2;

/*
 * @author Paul
 */
import Faces_Manifest_Project.ManifestTableModel;
import Faces_Manifest_Project.PatientList;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class VLMainForm extends javax.swing.JFrame {

    PatientList plPatient = new PatientList();
    ManifestTableModel model;
    //File sfile = new File("c:\\test.csv");
    public boolean importResultFlag = false;

    public VLMainForm() throws SQLException, IOException, ParseException {


        initComponents();
        myLabel.setText("");

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        updateViralList = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblManifest = new javax.swing.JTable();
        cmdclose = new javax.swing.JButton();
        loadExcel = new javax.swing.JButton();
        cmdSave = new javax.swing.JButton();
        myLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Viral Load Data Manager 2.0");
        setBackground(new java.awt.Color(102, 0, 204));
        setForeground(new java.awt.Color(51, 51, 0));
        setResizable(false);

        updateViralList.setText("Shipping Manifest");
        updateViralList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateViralListActionPerformed(evt);
            }
        });

        jScrollPane1.setViewportView(tblManifest);

        cmdclose.setText("Close");
        cmdclose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdcloseActionPerformed(evt);
            }
        });

        loadExcel.setText("Viral Load Results");
        loadExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadExcelActionPerformed(evt);
            }
        });

        cmdSave.setText("Save");
        cmdSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSaveActionPerformed(evt);
            }
        });

        myLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        myLabel.setText("Shipping Manifest");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cmdSave, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(updateViralList, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(loadExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmdclose, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(myLabel)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 606, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(myLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(updateViralList, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(loadExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(249, 249, 249)
                        .addComponent(cmdclose, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 387, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmdSave, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void updateViralListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateViralListActionPerformed

        try {
            importResultFlag = false;

            cmdSave.setText("Export Manifest");
            myLabel.setText("Shipping Manifest");
            model = new ManifestTableModel(plPatient.getPatientinfo()) {
            };
            tblManifest.repaint();

            tblManifest.setModel(model);
            jScrollPane1.setViewportView(tblManifest);
            tblManifest.removeColumn(tblManifest.getColumnModel().getColumn(7));
            tblManifest.removeColumn(tblManifest.getColumnModel().getColumn(6));
            tblManifest.removeColumn(tblManifest.getColumnModel().getColumn(5));

        } catch (Exception ex) {
            Logger.getLogger(VLMainForm.class.getName()).log(Level.SEVERE, null, ex);

        }
    }//GEN-LAST:event_updateViralListActionPerformed

    private void cmdcloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdcloseActionPerformed

        System.exit(0);

    }//GEN-LAST:event_cmdcloseActionPerformed

    private void loadExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadExcelActionPerformed
        try {
            importResultFlag = true;
            cmdSave.setText("Import Results");
            myLabel.setText("Importing Viral Load Results");

            model = new ManifestTableModel(plPatient.getExcelRecords()) {
            };

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "No records available to upload!!!");
//                uploadcsv.setEnabled(false);
            }

            tblManifest.setModel(model);
            jScrollPane1.setViewportView(tblManifest);
            tblManifest.removeColumn(tblManifest.getColumnModel().getColumn(4));
            tblManifest.removeColumn(tblManifest.getColumnModel().getColumn(3));
            tblManifest.removeColumn(tblManifest.getColumnModel().getColumn(2));
            tblManifest.removeColumn(tblManifest.getColumnModel().getColumn(1));

        } catch (SQLException ex) {
            Logger.getLogger(VLMainForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(VLMainForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(VLMainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_loadExcelActionPerformed

    private void cmdSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSaveActionPerformed
        
        int importRecords = plPatient.getActiveList().size();
        String importResultMessage = "Import Results:/n/nTotal Records: " + importRecords;
        
        try {


            if (importResultFlag) {
                plPatient.SaveExcelData();
                
                // Format message for display
                importResultMessage += "/n/nTotal Imported: " + (importRecords - plPatient.getActiveList().size());
                importResultMessage += "/n/nTotal Failed: " + plPatient.getActiveList().size();
                
                JOptionPane.showMessageDialog(null, importResultMessage);

                model = new ManifestTableModel(plPatient.getActiveList()) {
                };
                tblManifest.repaint();
            } else {
                myLabel.setVisible(true);
                plPatient.SaveManifest();
                JOptionPane.showMessageDialog(null, "Records Updated...");
                plPatient.generateManifest();
                JOptionPane.showMessageDialog(null, "Manifest Exported Successifully to Drive C...");
                model = new ManifestTableModel(plPatient.getPatientinfo()) {
                };
                tblManifest.repaint();
            }


        } catch (SQLException ex) {
            Logger.getLogger(VLMainForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(VLMainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_cmdSaveActionPerformed

    // JTable table = new JTable(model);
    public static void main(String args[]) {
        try {
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {

                    try {
                        new VLMainForm().setVisible(true);
                    } catch (SQLException ex) {
                        Logger.getLogger(VLMainForm.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(VLMainForm.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ParseException ex) {
                        Logger.getLogger(VLMainForm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        } catch (Exception ex) {
            Logger.getLogger(VLMainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdSave;
    private javax.swing.JButton cmdclose;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton loadExcel;
    private javax.swing.JLabel myLabel;
    private javax.swing.JTable tblManifest;
    private javax.swing.JButton updateViralList;
    // End of variables declaration//GEN-END:variables
}
