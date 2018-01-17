package trmasolucoes.com.br.testeormlite.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import trmasolucoes.com.br.testeormlite.cdp.Student;

/**
 * Created by tairo on 23/06/15.
 */
public class StudentDao extends BaseDaoImpl<Student, Integer> {

    public StudentDao(ConnectionSource connectionSource) throws SQLException {
        super(Student.class);
        setConnectionSource(connectionSource);
        initialize();
    }
}
