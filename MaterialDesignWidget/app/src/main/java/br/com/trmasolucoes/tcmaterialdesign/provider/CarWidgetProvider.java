package br.com.trmasolucoes.tcmaterialdesign.provider;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import br.com.trmasolucoes.tcmaterialdesign.MainActivity;
import br.com.trmasolucoes.tcmaterialdesign.R;
import br.com.trmasolucoes.tcmaterialdesign.service.CarWidgetService;

/**
 * Created by tairo on 14/02/16.
 */
public class CarWidgetProvider extends AppWidgetProvider {

    public static final String LOAD_CARS = "br.com.trmasolucoes.tcmaterialdesign.provider.LOAD_CARS";
    public static final String FILTER_CAR = "br.com.trmasolucoes.tcmaterialdesign.provider.FILTER_CAR";
    public static final String FILTER_CAR_ITEM = "br.com.trmasolucoes.tcmaterialdesign.provider.FILTER_CAR_ITEM";

    @Override
    public void onReceive(Context context, Intent intent) {

        /** Atribuio um gerenciador para o widget */
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        /** verifico se a intent não é nula */
        if (intent != null) {
            if (intent.getAction().equalsIgnoreCase(LOAD_CARS)) {

                /** pego a id do item do widget*/
                int appWidgetId  = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

                /** Verifico de o id é valido*/
                if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                   // appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_collection);
                    onUpdate(context, appWidgetManager, new int[]{appWidgetId});
                }

            }else if (intent.getAction().equalsIgnoreCase(FILTER_CAR)){

                String urlPhoto = intent.getStringExtra(FILTER_CAR_ITEM);
                Intent intentMainActivity = new Intent(context, MainActivity.class);
                intentMainActivity.putExtra(FILTER_CAR_ITEM, urlPhoto);
                intentMainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intentMainActivity);
            }
        }

        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int i = 0; i < appWidgetIds.length; i++) {
            Intent intentService = new Intent(context, CarWidgetService.class);
            intentService.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);

            //Acesso as vies de layout com RemoteView
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget_collection);

            //Carrega a lista de carros para o widget
            views.setRemoteAdapter(R.id.lv_collection, intentService);

            //Seta o texte quando a lista for vazia
            views.setEmptyView(R.id.lv_collection, R.id.tv_loanding);

            /**Listener de click na imagem*/
            Intent intentLoadCars = new Intent(context, CarWidgetProvider.class);//Declara a intent
            intentLoadCars.setAction(LOAD_CARS); //Mudo a action
            intentLoadCars.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);//seta o id do widget
            PendingIntent pendingIntentLoadCars = PendingIntent.getBroadcast(context, 0, intentLoadCars, 0);//Seta o pendingIntent para o click
            /** Vincula o click a imagem para atualizar a lista de carros */
            views.setOnClickPendingIntent(R.id.iv_update_collection, pendingIntentLoadCars);

            /**Listener de click na imagem para abrir o app*/
            Intent intentOpen = new Intent(context, MainActivity.class);//Declara a intent
            PendingIntent pendingIntentOpen = PendingIntent.getActivity(context, 0, intentOpen, 0);//Seta o pendingIntent para o click
            /** Vincula o click a imagem para abrir o app */
            views.setOnClickPendingIntent(R.id.iv_open_app, pendingIntentOpen);


            /** Seta o click para cada item da lista  do widget */
            Intent intentFilterCar = new Intent(context, CarWidgetProvider.class);
            intentFilterCar.setAction(FILTER_CAR);
            PendingIntent pendingIntentFilterCar = PendingIntent.getBroadcast(context, 0, intentFilterCar, 0);
            views.setPendingIntentTemplate(R.id.lv_collection, pendingIntentFilterCar); // Set o template que vai receber o click
            appWidgetManager.updateAppWidget(appWidgetIds[i], views); //Vincula o click no widget manger

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds[i], R.id.lv_collection);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
