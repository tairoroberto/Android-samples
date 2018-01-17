package br.com.trmasolucoes.tcmaterialdesign.service;

import android.content.Intent;
import android.widget.RemoteViewsService;

import br.com.trmasolucoes.tcmaterialdesign.adapters.CarWidgetFactoryAdapter;

/**
 * Created by tairo on 14/02/16.
 */
public class CarWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new CarWidgetFactoryAdapter(this, intent);
    }
}
