package trmasolucoes.com.br.testeormlite;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RawRowMapper;

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import trmasolucoes.com.br.testeormlite.cdp.Discipline;
import trmasolucoes.com.br.testeormlite.cdp.Student;
import trmasolucoes.com.br.testeormlite.dao.DatabaseHelper;
import trmasolucoes.com.br.testeormlite.dao.DisciplineDao;
import trmasolucoes.com.br.testeormlite.dao.StudentDao;


public class MainActivity extends ActionBarActivity {

    private DatabaseHelper helper;
    private StudentDao studentDao;
    private DisciplineDao disciplineDao;
    private List<Student> students;
    private Student student;
    private int firstId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //iInstacia a lista e adicionas os estudantes
        students = new ArrayList<Student>();
        student = new Student();
        student.setName("Tairo Roberto");
        student.setDisciplines(Arrays.asList(new Discipline("Android Nativo", "ADN"), new Discipline("Laravel 5", "Lav5")));

        students.add(student);

        students = new ArrayList<Student>();
        student = new Student();
        student.setName("Panico Bass");
        student.setDisciplines(Arrays.asList(new Discipline("Android Nativo", "ADN"), new Discipline("Laravel 5","Lav5")));

        students.add(student);

        //instancia o helper pra manipular a dase de dados
        helper = new DatabaseHelper(MainActivity.this);

        try {
            studentDao = new StudentDao(helper.getConnectionSource());
            disciplineDao = new DisciplineDao(helper.getConnectionSource());

            //Faz a insersão dos dados na base - CREATE
            for(Student student : students){
                int result = studentDao.create(student);
                //verifica se foi cadastrado na base
                if (result == 1){
                    for (Discipline discipline : student.getDisciplines()){
                        //adiciona o estudante a disciplina para fazer o mapeamento
                        discipline.setStudent(student);
                        disciplineDao.create(discipline);
                    }
                    //teste para ver o Id que é gerado
                    firstId = firstId == 0 ? student.getId() : firstId;
                }
            }

            //Mostra todos os dados e faz update - SELECT AND UPDATE
            Log.i("Script","GET ALL LINES");
            students = studentDao.queryForAll();
            for (Student student : students){
                Log.i("Script","Nome: " + student.getName() + " \n ID: " + student.getId() + "\n Disciplinas: " + student.getDisciplines().size());

                for (Discipline discipline : student.getDisciplines()){
                    Log.i("Script","Nome Diciplina: " + discipline.getName() + " \n ID: " + discipline.getId() + "\n Codigo: " + discipline.getCode());
                }

                //Adiciona mais uma Diciplina a grade do aluno
                Discipline d = new Discipline("Java","JAVA");
                d.setStudent(student);
                disciplineDao.create(d);

                //Ja faz o update do Estudante
                student.setName(student.getName() + " Miguel de Assunção");
                studentDao.update(student);
            }


            //Mostra todos os dados de novo sem alterações- SELECT
            Log.i("Script","GET ALL LINES AGAIN");
            students = studentDao.queryForAll();
            for (Student student : students){
                Log.i("Script", "Nome: " + student.getName() + " \n ID: " + student.getId() + "\n Disciplinas: " + student.getDisciplines().size());

                for (Discipline discipline : student.getDisciplines()){
                    Log.i("Script","Nome Diciplina: " + discipline.getName() + " \n ID: " + discipline.getId() + "\n Codigo: " + discipline.getCode());
                }
            }


            //Mostra os dados de uma determinada linha- SELECT BY ID
            Log.i("Script","GET ESPECIFIC LINE BY ID");
            student = studentDao.queryForId(firstId);

            Log.i("Script", "Nome: " + student.getName() + " \n ID: " + student.getId() + "\n Disciplinas: " + student.getDisciplines().size());

            for (Discipline discipline : student.getDisciplines()){
                Log.i("Script","Nome Diciplina: " + discipline.getName() + " \n ID: " + discipline.getId() + "\n Codigo: " + discipline.getCode());
            }

            //deleta todas as disciplinas do estudante
            disciplineDao.delete(student.getDisciplines());


            //Mostra os dados de uma determinado nome- SELECT BY NAME
            Log.i("Script","GET ESPECIFIC NAME");
            Map<String,Object> values = new HashMap<String, Object>();
            values.put("name","Panico Bass Miguel de Assunção");

            students = studentDao.queryForFieldValues(values);
            for (Student student : students){
                Log.i("Script", "Nome: " + student.getName() + " \n ID: " + student.getId() + "\n Disciplinas: " + student.getDisciplines().size());

                for (Discipline discipline : student.getDisciplines()){
                    Log.i("Script","Nome Diciplina: " + discipline.getName() + " \n ID: " + discipline.getId() + "\n Codigo: " + discipline.getCode());

                    //deleta as disciplinas uma por uma
                    disciplineDao.delete(discipline);
                }

            }


            //Mostra todos os dados de novo sem alterações- SELECT
            Log.i("Script", "GET ALL LINES AGAIN BY RAW");
            GenericRawResults<Student> raw = studentDao.queryRaw("SELECT id, name FROM student WHERE name like \"Panico%\"", new RawRowMapper<Student>() {
                @Override
                public Student mapRow(String[] collumnNames, String[] results) throws SQLException {
                    return new Student(Integer.parseInt(results[0]),results[1]);
                }
            });

            for (Student student : raw){
                Log.i("Script", "Nome: " + student.getName() + " \n ID: " + student.getId());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        helper.close();
    }
}
