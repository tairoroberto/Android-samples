package br.com.trmasolucoes.testeexpandablelistview;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private ExpandableListView expandableListView;
    private List<String> listGroup;
    private HashMap<String, List<String>> listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        expandableListView = (ExpandableListView) findViewById(R.id.expandedListView);

        buildList();
        expandableListView.setAdapter(new ExpandableAdapter(MainActivity.this, listGroup, listData));

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(MainActivity.this, "GROUP: " + groupPosition + " ITEM: " + childPosition, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(MainActivity.this, "GROUP (expand): " + groupPosition, Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(MainActivity.this, "GROUP (collapse): " + groupPosition, Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setGroupIndicator(getResources().getDrawable(R.drawable.icon_group));

    }

    public void buildList(){
        listGroup = new ArrayList<String>();
        listData = new HashMap<String, List<String>>();

        //GROUP
        listGroup.add("Group 1");
        listGroup.add("Group 2");
        listGroup.add("Group 3");
        listGroup.add("Group 4");

        //ITEM
        List<String> listAux = new ArrayList<String>();
        listAux.add("Item 5");
        listAux.add("Item 6");
        listAux.add("Item 7");
        listAux.add("Item 8");

        listData.put(listGroup.get(0),listAux);

        listAux = new ArrayList<String>();
        listAux.add("Item 9");
        listAux.add("Item 10");
        listAux.add("Item 11");
        listAux.add("Item 12");

        listData.put(listGroup.get(1),listAux);

        listAux = new ArrayList<String>();
        listAux.add("Item 12");
        listAux.add("Item 14");
        listAux.add("Item 15");
        listAux.add("Item 16");

        listData.put(listGroup.get(2),listAux);

        listAux = new ArrayList<String>();
        listAux.add("Item 17");
        listAux.add("Item 18");
        listAux.add("Item 19");
        listAux.add("Item 20");

        listData.put(listGroup.get(3),listAux);
    }
}
