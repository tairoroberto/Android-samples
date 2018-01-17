package trmasolucoes.com.br.testeormlite.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import trmasolucoes.com.br.testeormlite.cdp.Discipline;

/**
 * Created by tairo on 23/06/15.
 */
public class DisciplineDao extends BaseDaoImpl<Discipline,Integer> {

    public DisciplineDao(ConnectionSource connectionSource) throws SQLException {
        super(Discipline.class);
        setConnectionSource(connectionSource);
        initialize();
    }
}
