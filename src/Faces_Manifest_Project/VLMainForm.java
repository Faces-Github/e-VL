package Faces_Manifest_Project;

/*
 * @author Paul
 */

import static Faces_Manifest_Project.Global.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.TableColumn;

public class VLMainForm extends javax.swing.JFrame {

    public PatientList plPatient = new PatientList();
    ManifestTableModel model;
    File sfile = new File("c:\\test.csv");
    public boolean importResultFlag = false;
    private DataAccessClass dao;
    private List<String> location;
    private PrintStream standardOut;
    
    

    public VLMainForm() throws SQLException, IOException, ParseException {

        initComponents();
        myLabel.setText("");
        dao = new DataAccessClass();
        cboLocation.removeAllItems();
        location = new ArrayList<String>();
        location = dao.getLocations();
        
        for(String facility : location)
        {
            cboLocation.addItem(facility);
        }
//        PrintStream printStream = new PrintStream(new CustomOutputStream(this.jTextArea1));
//        this.standardOut = System.out;
//        System.setOut(printStream);
//        System.setErr(printStream);
        

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
        cboLocation = new javax.swing.JComboBox();
        lblMenuButtons = new javax.swing.JLabel();
        btnReviewCheckDigits = new javax.swing.JButton();
        btnSelectAll = new javax.swing.JButton();
        btnViewShippedRequests = new javax.swing.JButton();
        uploadShippingManifest = new javax.swing.JButton();
        lblStatusUpdate = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        ShippingStartDate = new org.jdesktop.swingx.JXDatePicker();
        ShippingEndDate = new org.jdesktop.swingx.JXDatePicker();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Viral Load Manifest Form");
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

        cboLocation.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboLocation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboLocationActionPerformed(evt);
            }
        });

        lblMenuButtons.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblMenuButtons.setText("Select Location");

        btnReviewCheckDigits.setText("Review Check Digits");
        btnReviewCheckDigits.setEnabled(false);
        btnReviewCheckDigits.setName("btnReviewCheckDigits"); // NOI18N
        btnReviewCheckDigits.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReviewCheckDigitsActionPerformed(evt);
            }
        });

        btnSelectAll.setText("Select All");
        btnSelectAll.setEnabled(false);
        btnSelectAll.setName("btnSelectAll"); // NOI18N
        btnSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectAllActionPerformed(evt);
            }
        });

        btnViewShippedRequests.setText("View Shipped Requests");
        btnViewShippedRequests.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewShippedRequestsActionPerformed(evt);
            }
        });

        uploadShippingManifest.setText("Upload Shipping Manifest");
        uploadShippingManifest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uploadShippingManifestActionPerformed(evt);
            }
        });

        lblStatusUpdate.setForeground(new java.awt.Color(0, 0, 255));

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);
        jTextArea1.getAccessibleContext().setAccessibleName("");

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Progress Output");

        jLabel2.setText("Shipping Start Date");

        jLabel3.setText("Shipping End Date");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmdclose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblMenuButtons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(ShippingEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(cboLocation, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(updateViralList, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(uploadShippingManifest, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnViewShippedRequests, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(ShippingStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(loadExcel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSeparator4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblStatusUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 6, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(myLabel)
                        .addGap(556, 556, 556)
                        .addComponent(jLabel1)
                        .addGap(149, 149, 149))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnReviewCheckDigits, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(71, 71, 71)
                                .addComponent(btnSelectAll, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(88, 88, 88)
                                .addComponent(cmdSave, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 679, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(myLabel)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cmdSave, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnSelectAll, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnReviewCheckDigits, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jScrollPane2))
                        .addGap(16, 16, 16))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblMenuButtons, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboLocation, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ShippingStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ShippingEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(updateViralList, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16)
                        .addComponent(uploadShippingManifest, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnViewShippedRequests, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(loadExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                                .addComponent(lblStatusUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addComponent(cmdclose, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void updateViralListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateViralListActionPerformed

        try {
             PrintStream printStream = new PrintStream(new CustomOutputStream(this.jTextArea1));
        this.standardOut = System.out;
        System.setOut(printStream);
        System.setErr(printStream);
            importResultFlag = false;

            cmdSave.setText("Export Manifest");
            myLabel.setText("Shipping Manifest");

            // Show processing status
            lblStatusUpdate.setText("Getting list of samples for shipping.\nPlease wait...");
            lblStatusUpdate.paintImmediately(lblStatusUpdate.getVisibleRect());
            
            model = new ManifestTableModel(plPatient.getPatientinfo()) {
            };
            tblManifest.repaint();

            // Show processing status
            lblStatusUpdate.setText("");
            lblStatusUpdate.paintImmediately(lblStatusUpdate.getVisibleRect());
            
            tblManifest.setModel(model);
            jScrollPane1.setViewportView(tblManifest);
            tblManifest.removeColumn(tblManifest.getColumnModel().getColumn(8));
            tblManifest.removeColumn(tblManifest.getColumnModel().getColumn(7));
            tblManifest.removeColumn(tblManifest.getColumnModel().getColumn(6));
            tblManifest.removeColumn(tblManifest.getColumnModel().getColumn(5));
            
             //set column size
            TableColumn column = null;
            column = tblManifest.getColumnModel().getColumn(0); //Identifier column
            column.setPreferredWidth(40);
            column = tblManifest.getColumnModel().getColumn(4); //Identifier column
            column.setPreferredWidth(30);

            column = tblManifest.getColumnModel().getColumn(1); //Identifier column
            column.setPreferredWidth(150);

            // Enable/Disable Review Check Digit button
            btnReviewCheckDigits.setEnabled(importResultFlag && model.getRowCount() > 0);
            
            // Enable/Disable Select All button
            if(tblManifest.getRowCount() > 0){
                btnSelectAll.setEnabled(true);
            }
            else{
                btnSelectAll.setEnabled(false);
            }
            
                        
        } catch (Exception ex) {
            Logger.getLogger(VLMainForm.class.getName()).log(Level.SEVERE, null, ex);

        }
       
    }//GEN-LAST:event_updateViralListActionPerformed

    private void cmdcloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdcloseActionPerformed

        System.exit(0);

    }//GEN-LAST:event_cmdcloseActionPerformed

    private void loadExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadExcelActionPerformed
        
        try {
//            PrintStream printStream = new PrintStream(new CustomOutputStream(this.jTextArea1));
//        this.standardOut = System.out;
//        System.setOut(printStream);
//        System.setErr(printStream);
            importResultFlag = true;
            cmdSave.setText("Import Results");
            cmdSave.setEnabled(true);
            myLabel.setText("Importing Viral Load Results");

            // Display dialog to select import source
            VLResultsImport frmResults = new VLResultsImport();
            JDialog dlg = new JDialog(this, "Specify Viral Load Import Source", true);
            frmResults.doModal(dlg);

            // Check if importation has been canceled
            if(cancelImport){
                return;
            }
            
            // Show processing status
            lblStatusUpdate.setText("Getting VL results from testing lab.\nPlease wait...");
            lblStatusUpdate.paintImmediately(lblStatusUpdate.getVisibleRect());
            
            // Import data depending on what has been selected
            if(resultOption == 0){
                
                // Get results from AMPATH website
//                JOptionPane.showMessageDialog(null, plPatient.getAMPATHVLResults());
                model = new ManifestTableModel(plPatient.getAMPATHVLResults()) {};
//                plPatient.getAMPATHVLResults();
            }
            else {
                
                // Get results from MS Excel file
                model = new ManifestTableModel(plPatient.getImportRecords()) {};

            }

            // Show processing status
            lblStatusUpdate.setText("");
            lblStatusUpdate.paintImmediately(lblStatusUpdate.getVisibleRect());
            
            
            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "No records available to import!!!");
//                uploadcsv.setEnabled(false);
            }

            tblManifest.setModel(model);
            jScrollPane1.setViewportView(tblManifest);
            tblManifest.removeColumn(tblManifest.getColumnModel().getColumn(4));
//            tblManifest.removeColumn(tblManifest.getColumnModel().getColumn(3));
            tblManifest.removeColumn(tblManifest.getColumnModel().getColumn(2));
            tblManifest.removeColumn(tblManifest.getColumnModel().getColumn(1));
            
            // Enable/Disable Review Check Digit button
            btnReviewCheckDigits.setEnabled(importResultFlag && model.getRowCount() > 0);
            
        } 
        catch (SQLException ex) {
            Logger.getLogger(VLMainForm.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (IOException ex) {
            Logger.getLogger(VLMainForm.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (ParseException ex) {
            Logger.getLogger(VLMainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_loadExcelActionPerformed

    private void cmdSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSaveActionPerformed
        PrintStream printStream = new PrintStream(new CustomOutputStream(this.jTextArea1));
        this.standardOut = System.out;
        System.setOut(printStream);
        System.setErr(printStream);
        
        int importRecords = plPatient.getActiveList().size();
        String importResultMessage = "Import Results\n\nTotal Import Records: " + importRecords;
        String failedImportRecord = "";
        String logDate = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss").format(new Date());
        
        try {


            if (importResultFlag) {
                
//                plPatient.SaveExcelData();
                plPatient.saveViralLoadResults();
                
                // Format Import results message
                importResultMessage += "\n\nTotal Successful Imports: " + (importRecords - plPatient.getActiveList().size());
                importResultMessage += "\n\nTotal Failed Imports: " + plPatient.getActiveList().size();
                
                JOptionPane.showMessageDialog(null, importResultMessage);

                // Save any unimported records in a text log file
                if(plPatient.getActiveList().size() > 0){
                    
                    PrintWriter pwOutputFile = new PrintWriter("VL_UnImported_Records_" + logDate + ".log");

                    // Write file headers
                    pwOutputFile.println("Facility,\tPatient ID,\tSample Date,\tResult,\tLog Copies");

                    // Loop through list and log
                    for (PatientDetails patient : plPatient.getActiveList()) {

                        // Build the record for writing
                        failedImportRecord = patient.getFacility() + ",\t" + 
                                                patient.getPatientID() + ",\t" + 
                                                patient.getDateRun().toString() + ",\t" + 
                                                patient.getVLResult() + ",\t" + 
                                                patient.getLogResult() + ",\t" + patient.getComments();

                        pwOutputFile.println(failedImportRecord);
                    }

                    // Close log file
                    pwOutputFile.close();
                }
                
                model = new ManifestTableModel(plPatient.getActiveList()) {
                };
                tblManifest.repaint();
            } else {
                
                // Post Records to AMPATH website
                noInternetConnection = false;
                plPatient.postAMPATHVLRequests();
                // Post Records to AMPATH website
                
                myLabel.setVisible(true);
                plPatient.SaveManifest();
                JOptionPane.showMessageDialog(null, "Sample records have been updated as shipped.\n\nIf you had an Internet connection problem, use the generated manifest text file to retry uploading to testing lab website later.");
                plPatient.generateManifest();
                JOptionPane.showMessageDialog(null, "Shipping Manifest File Generated Successfully!");
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

    private void btnReviewCheckDigitsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReviewCheckDigitsActionPerformed
        
        // Go through Patient IDs and review check digits and regenerate if found
        plPatient.reviewPatientID();;
        
    }//GEN-LAST:event_btnReviewCheckDigitsActionPerformed

    private void btnSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectAllActionPerformed
        
        // (Un)Select all the samples that have to be shipped
        try {
            PrintStream printStream = new PrintStream(new CustomOutputStream(this.jTextArea1));
        this.standardOut = System.out;
        System.setOut(printStream);
        System.setErr(printStream);
            
            if (!importResultFlag){

                // Set all the patients in the list as shipped and then refresh the display
                if(btnSelectAll.getText().equalsIgnoreCase("Select All")){
                    plPatient.SelectAll(true);
                    btnSelectAll.setText("Unselect All");
                }
                else {
                    plPatient.SelectAll(false);
                    btnSelectAll.setText("Select All");
                }

            }

            // Repaint the table
            tblManifest.repaint();
        }
        catch (Exception ex) {
            Logger.getLogger(VLMainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_btnSelectAllActionPerformed

    private void btnViewShippedRequestsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewShippedRequestsActionPerformed

        try {
//            PrintStream printStream = new PrintStream(new CustomOutputStream(this.jTextArea1));
//        this.standardOut = System.out;
//        System.setOut(printStream);
//        System.setErr(printStream);
            importResultFlag = false;

            cmdSave.setText("Export Manifest");
            myLabel.setText("Shipped VL Requests");
            model = new ManifestTableModel(plPatient.getShippedManifestList()) {};
            tblManifest.repaint();

            tblManifest.setModel(model);
            jScrollPane1.setViewportView(tblManifest);
            tblManifest.removeColumn(tblManifest.getColumnModel().getColumn(8));
            tblManifest.removeColumn(tblManifest.getColumnModel().getColumn(7));
            tblManifest.removeColumn(tblManifest.getColumnModel().getColumn(6));
            tblManifest.removeColumn(tblManifest.getColumnModel().getColumn(5));
            
             //set coulmun size
            TableColumn column = null;
            column = tblManifest.getColumnModel().getColumn(0); //Identifier column
            column.setPreferredWidth(40);
            column = tblManifest.getColumnModel().getColumn(4); //Identifier column
            column.setPreferredWidth(30);

            column = tblManifest.getColumnModel().getColumn(1); //Identifier column
            column.setPreferredWidth(150);

            // Enable/Disable Review Check Digit button
            btnReviewCheckDigits.setEnabled(false);
            
            // Enable/Disable Select All button
            if(tblManifest.getRowCount() > 0){
                btnSelectAll.setEnabled(true);
                btnSelectAll.setText("Unselect All");
            }
            else{
                btnSelectAll.setEnabled(false);
            }
            
                        
        } catch (Exception ex) {
            Logger.getLogger(VLMainForm.class.getName()).log(Level.SEVERE, null, ex);

        }
        
    }//GEN-LAST:event_btnViewShippedRequestsActionPerformed

    private void uploadShippingManifestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadShippingManifestActionPerformed
        
        // Upload existing shipping manifest to AMPATH website
        
        try {
            
            // Initialize variables
            String msgSuccess = "";
            int iCountSuccess = 0;
            
            // Prompt user to select MS Excel file shipping manifest
            JFileChooser excelFile = new JFileChooser();
            int btnClicked = excelFile.showOpenDialog(null);
            
            // If Cancel is selected, exit
            if (btnClicked == JFileChooser.CANCEL_OPTION){
                return;
            }
            
            // Get the specified file
            dataFile = excelFile.getSelectedFile();

            // Open the file and access contents

            // Extract contents into Patients Array List
            plPatient.getImportManifest();

            // Display the records and ask user to confirm upload
            model = new ManifestTableModel(plPatient.getActiveList()) {};

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "No records available!");
            }

            tblManifest.setModel(model);
            jScrollPane1.setViewportView(tblManifest);
            tblManifest.removeColumn(tblManifest.getColumnModel().getColumn(8));
            tblManifest.removeColumn(tblManifest.getColumnModel().getColumn(7));
            tblManifest.removeColumn(tblManifest.getColumnModel().getColumn(6));
            tblManifest.removeColumn(tblManifest.getColumnModel().getColumn(5));
            
            int reply = JOptionPane.showConfirmDialog(null, 
                                    plPatient.getActiveList().size() + " records will be uploaded to AMPATH website. Continue?", 
                                    "Upload Shipping Manifest", JOptionPane.YES_NO_OPTION);
            
            if (reply == JOptionPane.YES_OPTION) {
                // Upload to AMPATH website
                Faces_Manifest_Project.Global.uploadShippingManifest = true;
                
                noInternetConnection = false;
                plPatient.postAMPATHVLRequests();
                
                Faces_Manifest_Project.Global.uploadShippingManifest = false;
                
                // Check if any records failed
                if(!noInternetConnection){
                    
                    for (PatientDetails patient : plPatient.getActiveList()) {
                        if(patient.isImported()){
                           iCountSuccess = iCountSuccess + 1 ; 
                        }
                    }

                    // Display upload success rate
                    JOptionPane.showMessageDialog(null, iCountSuccess + "/" + plPatient.getActiveList().size() + " records successfully uploaded to AMPATH website!");

                }
            }

        }
        catch (SQLException ex) {
            Logger.getLogger(VLMainForm.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (IOException ex) {
            Logger.getLogger(VLMainForm.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (ParseException ex) {
            Logger.getLogger(VLMainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Disable the Save button
        cmdSave.setEnabled(false);
        PrintStream printStream = new PrintStream(new CustomOutputStream(this.jTextArea1));
        this.standardOut = System.out;
        System.setOut(printStream);
        System.setErr(printStream);
    }//GEN-LAST:event_uploadShippingManifestActionPerformed

    private void cboLocationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboLocationActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboLocationActionPerformed

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
        } 
        catch (Exception ex) {
            Logger.getLogger(VLMainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static org.jdesktop.swingx.JXDatePicker ShippingEndDate;
    public static org.jdesktop.swingx.JXDatePicker ShippingStartDate;
    private javax.swing.JButton btnReviewCheckDigits;
    private javax.swing.JButton btnSelectAll;
    private javax.swing.JButton btnViewShippedRequests;
    public static javax.swing.JComboBox cboLocation;
    private javax.swing.JButton cmdSave;
    private javax.swing.JButton cmdclose;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator4;
    public javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel lblMenuButtons;
    private javax.swing.JLabel lblStatusUpdate;
    private javax.swing.JButton loadExcel;
    private javax.swing.JLabel myLabel;
    private javax.swing.JTable tblManifest;
    private javax.swing.JButton updateViralList;
    private javax.swing.JButton uploadShippingManifest;
    // End of variables declaration//GEN-END:variables
}
