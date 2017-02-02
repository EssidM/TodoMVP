package com.leadit.todomvp.database.config;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;
import com.leadit.todomvp.database.tables.NoteTable;
import com.leadit.todomvp.utils.Constants;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Data base configuration class
 * <p>
 * generates a configuration file which be placed on res/raw directory
 *
 * @author Mohamed Essid on 02/02/2017.
 */

public class DatabaseConfigUtil extends OrmLiteConfigUtil {

    private static final Class[] tables = {NoteTable.class};

    public static void main(String args[]) throws IOException, SQLException {
        //provide the name of .txt file that its already created and placed in res/raw directory
        writeConfigFile(Constants.ORMLITE_CONFIG_FILE_NAME, tables);
    }
}
