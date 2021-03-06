package br.com.trmasolucoes.tcmaterialdesign.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.List;

import br.com.trmasolucoes.tcmaterialdesign.R;
import br.com.trmasolucoes.tcmaterialdesign.domain.Car;
import br.com.trmasolucoes.tcmaterialdesign.extras.ImageHelper;
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
    private int width;
    private int height;

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

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int position) {

        myViewHolder.tvModel.setText(mList.get(position).getModel());
        myViewHolder.tvBrand.setText(mList.get(position).getBrand());

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            myViewHolder.ivCar.setImageResource(mList.get(position).getPhoto());
        }
        else{
            Bitmap bitmap = BitmapFactory.decodeResource( mContext.getResources(), mList.get(position).getPhoto());
            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);

            bitmap = ImageHelper.getRoundedCornerBitmap(mContext, bitmap, 4, width, height, false, false, true, true);
            myViewHolder.ivCar.setImageBitmap(bitmap);
        }

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
        public ImageView ivCar;
        public TextView tvModel;
        public TextView tvBrand;

        public MyViewHolder(View itemView) {
            super(itemView);

            ivCar = (ImageView) itemView.findViewById(R.id.iv_car);
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
