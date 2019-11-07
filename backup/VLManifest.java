/*
 * @author Paul
 */
package manifetproject2;

import java.sql.SQLException;

public class VLManifest {

    public static void main(String[] args) throws SQLException {
        try {
            VLMainForm displayrecords = new VLMainForm();
            displayrecords.setVisible(true);
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
