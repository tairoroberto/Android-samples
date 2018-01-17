package br.com.trmasolucoes.tcmaterialdesign.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;

import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import br.com.trmasolucoes.tcmaterialdesign.MainActivity;
import br.com.trmasolucoes.tcmaterialdesign.R;
import br.com.trmasolucoes.tcmaterialdesign.domain.Car;
import br.com.trmasolucoes.tcmaterialdesign.extras.DataUrl;
import br.com.trmasolucoes.tcmaterialdesign.provider.CarWidgetProvider;

/**
 * Created by tairo on 14/02/16.
 */
public class CarWidgetFactoryAdapter implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private List<Car> mList;
    private int size;

    public CarWidgetFactoryAdapter(Context mContext, Intent intent) {
        this.mContext = mContext;
        float scale = mContext.getResources().getDisplayMetrics().density;
        size = (int) (50 * scale + 0.5f);
    }

    @Override
    public void onCreate() {
        mList = new MainActivity().getSetCarList(10);
    }

    @Override
    public void onDataSetChanged() {
        Collections.shuffle(mList, new Random());
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mList.size(); //Número de elementos
    }

    @Override
    public RemoteViews getViewAt(int position) {

        /** Pega o view root*/
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.app_widget_item);

        /** Seto os textviews*/
        views.setTextViewText(R.id.tv_model, mList.get(position).getModel());
        views.setTextViewText(R.id.tv_brand, mList.get(position).getBrand());

        /** Faz o dowload da imagem do carro para a imageView*/
        try {
            Bitmap myBitmap = Glide.with(mContext)
                    .load(DataUrl.getUrlCustom(mList.get(position).getUrlPhoto(), size))
                    .asBitmap()
                    .centerCrop()
                    .into(500, 500)
                    .get();

            /** Seto a imagem baixada para  imageview*/
         views.setImageViewBitmap(R.id.iv_car, myBitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }

        /** Filtro para pegar o click da view*/
        Intent intentFiltro = new Intent();
        intentFiltro.putExtra(CarWidgetProvider.FILTER_CAR_ITEM, mList.get(position).getUrlPhoto());

        /** Atribuo o listener a view*/
        views.setOnClickFillInIntent(R.id.lr_container, intentFiltro);
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1; //Quantidade de tipos que estamos trabalhando no widget
    }

    @Override
    public long getItemId(int position) {
        return position; // Posição do item
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
