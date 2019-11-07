package Faces_Manifest_Project;

/*
 * @author Paul
 */
import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

//import net.codejava.model.Employee;
public abstract class ManifestTableModel extends AbstractTableModel implements TableModelListener, TableModel, Serializable {

//   getting patient list
    private final List<PatientDetails> lOfPatients;
//    introducing table columns
    private final String[] columnNames = new String[]{
        "Patient_ID", "Names", "Facility", "Sample_Date", "shipped", "Result", "logResult", "RunDate", "Comments"
    };
    private final Class[] columnClass = new Class[]{
        String.class, String.class, String.class, Date.class, boolean.class, String.class, String.class, Date.class, String.class
    };

//    setting patient list
    public ManifestTableModel(List<PatientDetails> lOfPatients) {
        this.lOfPatients = lOfPatients;
    }

    // returns the column name for the table
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 4) {
            return getValueAt(0, 4).getClass();
        }
        return columnClass[columnIndex];
    }

    // returns the number of columns in the table
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    // returns the number of rows in the table
    @Override
    public int getRowCount() {
        return lOfPatients.size();
    }

// returns the value from cell  .
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        // Instantiate return object
        Object objReturn = null;

        try {
            PatientDetails row = lOfPatients.get(rowIndex);

            switch (columnIndex) {
                case 0:
                    objReturn = row.getPatientID();
                    break;

                case 1:
                    objReturn = row.getPatientNames();
                    break;

                case 2:
                    objReturn = row.getFacility();
                    break;

                case 3:
                    objReturn = row.getSampleDate();
                    break;

                case 4:
                    objReturn = row.isShipped();
                    break;
                case 5:
                    objReturn = row.getVLResult();
                    break;
                case 6:
                    objReturn = row.getLogResult();
                    break;
                case 7:
                    objReturn = row.getDateRun();
                    break;
                case 8:
                    objReturn = row.getComments();
                    break;
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return objReturn;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 4) {
            return true;
        }
        return false;

    }

//    Editing of values in Jtable
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        try {
            PatientDetails row = lOfPatients.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    row.setPatientID((String) aValue);
                    break;
                case 1:
                    row.setPatientNames((String) aValue);
                    break;
                case 2:
                    row.setFacility((String) aValue);
                    break;
                case 3:
                    row.setSampleDate((Date) aValue);
                    break;
                case 4:
                    row.setShipped((boolean) aValue);
                    break;
                case 5:
                    row.setVLResult((String) aValue);
                    break;
                case 6:
                    row.setLogResult((String) aValue);
                    break;
                case 7:
                    row.setDateRun((Date) aValue);
                    break;
                case 8:
                    row.setComments((String) aValue);
                    break;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void tableChanged(TableModelEvent e) {
        int columnIndex = e.getColumn();
        TableModel model = (TableModel) e.getSource();
        String columnName = model.getColumnName(columnIndex);

    }
}
