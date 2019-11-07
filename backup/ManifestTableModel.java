package manifetproject2;

/*
 * @author Paul
 */
import Faces_Manifest_Project.PatientDetails;
import java.sql.Date;
import java.util.List;
import java.io.Serializable;
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
        "Patient_ID", "Names", "Facility", "Date", "shipped", "Result", "logResult", "Date"
    };
    private final Class[] columnClass = new Class[]{
        String.class, String.class, String.class, Date.class, boolean.class, String.class, String.class, Date.class
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
                    objReturn = row.getpatientid();
                    break;

                case 1:
                    objReturn = row.getpatientNames();
                    break;

                case 2:
                    objReturn = row.getfacility();
                    break;

                case 3:
                    objReturn = row.getsample_date();
                    break;

                case 4:
                    objReturn = row.isshipped();
                    break;
                case 5:
                    objReturn = row.getvResult();
                    break;
                case 6:
                    objReturn = row.getlogResult();
                    break;
                case 7:
                    objReturn = row.getdateRun();
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
        return true;

    }

//    Editing of values in Jtable
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        try {
            PatientDetails row = lOfPatients.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    row.setpatientid((String) aValue);
                    break;
                case 1:
                    row.setpatientNames((String) aValue);
                    break;
                case 2:
                    row.setfacility((String) aValue);
                    break;
                case 3:
                    row.setsample_date((Date) aValue);
                    break;
                case 4:
                    row.setshipped((boolean) aValue);
                    break;
                case 5:
                    row.setvResult((String) aValue);
                    break;
                case 6:
                    row.setlogResult((String) aValue);
                    break;
                case 7:
                    row.setdateRun((Date) aValue);
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
