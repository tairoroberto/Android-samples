package trmasolucoes.com.br.testeormlite.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import trmasolucoes.com.br.testeormlite.cdp.Discipline;
import trmasolucoes.com.br.testeormlite.cdp.Student;

/**
 * Created by tairo on 23/06/15.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper{

    private static final String databaseName = "school.db";
    private static final int databaseVersion = 1;
    private Context context;

    public DatabaseHelper(Context context) {
        super(context, databaseName, null, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {

            TableUtils.createTable(connectionSource, Student.class);
            TableUtils.createTable(connectionSource, Discipline.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            //Deletas as tabelas e cria de novo
            TableUtils.dropTable(connectionSource, Student.class,true);
            TableUtils.dropTable(connectionSource, Discipline.class, true);
            onCreate(sqLiteDatabase, connectionSource);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        super.close();
    }
}
