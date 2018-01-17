package br.com.trmasolucoes.tcmaterialdesign.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;
import java.util.Random;

import br.com.trmasolucoes.tcmaterialdesign.R;
import br.com.trmasolucoes.tcmaterialdesign.domain.Car;
import br.com.trmasolucoes.tcmaterialdesign.extras.DataUrl;
import br.com.trmasolucoes.tcmaterialdesign.interfaces.RecyclerViewOnClickListenerHack;

/**
 * Created by viniciusthiengo on 4/5/15.
 */
public class CarAdapter extends RecyclerView.Adapter<CarAdapter.MyViewHolder> {
    private Context mContext;
    private List<Car> mList;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;
    private float scale;
    private int width, height, roundPixels;

    private boolean withAnimation;
    private boolean withCardLayout;


    public CarAdapter(Context c, List<Car> l){
        this(c, l, true, true);
    }
    public CarAdapter(Context c, List<Car> l, boolean wa, boolean wcl){
        mContext = c;
        mList = l;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        withAnimation = wa;
        withCardLayout = wcl;

        scale = mContext.getResources().getDisplayMetrics().density;
        width = mContext.getResources().getDisplayMetrics().widthPixels - (int)(14 * scale + 0.5f);
        height = (width / 16) * 9;

        roundPixels = (int)(2 * scale + 0.5f);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;

        if(withCardLayout){
            v = mLayoutInflater.inflate(R.layout.item_car_card, viewGroup, false);
        }
        else{
            v = mLayoutInflater.inflate(R.layout.item_car, viewGroup, false);
        }

        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    public Random rand = new Random();

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int position) {

        myViewHolder.tvModel.setText(mList.get(position).getModel());
        myViewHolder.tvBrand.setText(mList.get(position).getBrand());


        ControllerListener listener = new BaseControllerListener(){
            @Override
            public void onFinalImageSet(String id, Object imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                Log.i("LOG", "onFinalImageSet");
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                super.onFailure(id, throwable);
                Log.i("LOG", "onFailure");
            }

            @Override
            public void onIntermediateImageFailed(String id, Throwable throwable) {
                super.onIntermediateImageFailed(id, throwable);
                Log.i("LOG", "onIntermediateImageFailed");
            }

            @Override
            public void onIntermediateImageSet(String id, Object imageInfo) {
                super.onIntermediateImageSet(id, imageInfo);
                Log.i("LOG", "onIntermediateImageSet");
            }

            @Override
            public void onRelease(String id) {
                super.onRelease(id);
                Log.i("LOG", "onRelease");
            }

            @Override
            public void onSubmit(String id, Object callerContext) {
                super.onSubmit(id, callerContext);
                Log.i("LOG", "onSubmit");
            }
        };

        int w = 0;
        if( myViewHolder.ivCar.getLayoutParams().width == FrameLayout.LayoutParams.MATCH_PARENT
            || myViewHolder.ivCar.getLayoutParams().width == FrameLayout.LayoutParams.WRAP_CONTENT){

            Display display = ( (Activity) mContext ).getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize( size );

            try{
                w = size.x;
            }
            catch( Exception e ){
                w = display.getWidth();
            }
        }

        Uri uri = Uri.parse(DataUrl.getUrlCustom( mList.get(position).getUrlPhoto(), w )  );
        DraweeController dc = Fresco.newDraweeControllerBuilder()
                .setUri( uri )
                .setTapToRetryEnabled(true)
                .setControllerListener( listener )
                .setOldController( myViewHolder.ivCar.getController() )
                .build();

        RoundingParams rp = RoundingParams.fromCornersRadii(roundPixels, roundPixels, 0, 0);
        myViewHolder.ivCar.setController(dc);
        myViewHolder.ivCar.getHierarchy().setRoundingParams( rp );

        /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            myViewHolder.ivCar.setImageResource(mList.get(position).getPhoto());
        }
        else{
            Bitmap bitmap = BitmapFactory.decodeResource( mContext.getResources(), mList.get(position).getPhoto());
            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);

            bitmap = ImageHelper.getRoundedCornerBitmap(mContext, bitmap, 4, width, height, false, false, true, true);
            myViewHolder.ivCar.setImageBitmap(bitmap);
        }*/

        if(withAnimation){
            try{
                YoYo.with(Techniques.Tada)
                        .duration(700)
                        .playOn(myViewHolder.itemView);
            }
            catch(Exception e){}
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r){
        mRecyclerViewOnClickListenerHack = r;
    }


    public void addListItem(Car c, int position){
        mList.add(c);
        notifyItemInserted(position);
    }


    public void removeListItem(int position){
        mList.remove(position);
        notifyItemRemoved(position);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public SimpleDraweeView ivCar;
        public TextView tvModel;
        public TextView tvBrand;

        public MyViewHolder(View itemView) {
            super(itemView);

            ivCar = (SimpleDraweeView) itemView.findViewById(R.id.iv_car);
            tvModel = (TextView) itemView.findViewById(R.id.tv_model);
            tvBrand = (TextView) itemView.findViewById(R.id.tv_brand);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mRecyclerViewOnClickListenerHack != null){
                mRecyclerViewOnClickListenerHack.onClickListener(v, getPosition());
            }
        }
    }
}
