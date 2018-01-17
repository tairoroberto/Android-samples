package trmasolucoes.com.br.testegradanddrop;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.img1).setOnLongClickListener(new MyOnLongClickListener());
        findViewById(R.id.img2).setOnLongClickListener(new MyOnLongClickListener());
        findViewById(R.id.img3).setOnLongClickListener(new MyOnLongClickListener());
        findViewById(R.id.img4).setOnLongClickListener(new MyOnLongClickListener());

        findViewById(R.id.topLeft).setOnDragListener(new MyOnDragListener(1));
        findViewById(R.id.topRight).setOnDragListener(new MyOnDragListener(2));
        findViewById(R.id.bottomLeft).setOnDragListener(new MyOnDragListener(3));
        findViewById(R.id.bottomRight).setOnDragListener(new MyOnDragListener(4));
    }

    class MyOnLongClickListener implements View.OnLongClickListener{
        @Override
        public boolean onLongClick(View view) {

            ClipData clipData = ClipData.newPlainText("simple_text","text");

            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(findViewById(R.id.shadaw));

            view.startDrag(clipData,shadowBuilder, view, 0);
            view.setVisibility(View.INVISIBLE);

            return true;
        }
    }

    class MyOnDragListener implements View.OnDragListener{

        private int num;
        public MyOnDragListener(int num) {
            super();
            this.num = num;
        }

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();

            switch (action){
                case DragEvent.ACTION_DRAG_STARTED:
                    Log.i("Script","NUM: "+ num + " - ACTION_DRAG_STARTED");
                    if(event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)){
                        return (true);
                    }
                    return (false);
                case DragEvent.ACTION_DRAG_ENTERED:
                    Log.i("Script","NUM: "+ num + " - ACTION_DRAG_ENTERED");
                    v.setBackgroundColor(Color.RED);
                    break;
                case DragEvent.ACTION_DRAG_LOCATION:
                    Log.i("Script","NUM: "+ num + " - ACTION_DRAG_LOCATION");
                    v.setBackgroundColor(Color.RED);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    Log.i("Script","NUM: "+ num + " - ACTION_DRAG_EXITED");
                    v.setBackgroundColor(Color.BLUE);
                    break;
                case DragEvent.ACTION_DROP:
                    Log.i("Script","NUM: "+ num + " - ACTION_DROP");
                    //pega o local da viw
                    View view = (View)event.getLocalState();
                    //pga o pai da view
                    ViewGroup parent = (ViewGroup) view.getParent();
                    //remove a view do pai
                    parent.removeView(view);

                    //copia o local da view
                    LinearLayout layout = (LinearLayout) v;
                    //Adiciona novamente a view ao novo pai
                    layout.addView(view);
                    //mostra a view novamente
                    view.setVisibility(View.VISIBLE);
                    break;

                case DragEvent.ACTION_DRAG_ENDED:
                    Log.i("Script","NUM: "+ num + " - ACTION_DRAG_ENDED");
                    v.setBackgroundColor(Color.BLUE);
                    break;
            }

            return true;
        }
    }
}
