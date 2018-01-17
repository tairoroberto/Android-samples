package br.com.trmasolucoes.tcmaterialdesign.fragments;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.trmasolucoes.tcmaterialdesign.MainActivity;
import br.com.trmasolucoes.tcmaterialdesign.R;
import br.com.trmasolucoes.tcmaterialdesign.adapters.CarAdapter;
import br.com.trmasolucoes.tcmaterialdesign.domain.Car;

public class LuxuryCarFragment extends CarFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            mList = savedInstanceState.getParcelableArrayList("mList");
        }
        else{
            mList = ((MainActivity) getActivity()).getCarsByCategory(1);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    fab.hideMenuButton(true);
                } else {
                    fab.showMenuButton(true);
                }

                GridLayoutManager llm = (GridLayoutManager) mRecyclerView.getLayoutManager();
                CarAdapter adapter = (CarAdapter) mRecyclerView.getAdapter();

                if (mList.size() == llm.findLastCompletelyVisibleItemPosition() + 1) {
                    List<Car> listAux = ((MainActivity) getActivity()).getSetCarList(10, 1);
                    ((MainActivity) getActivity()).getListCars().addAll(listAux);

                    for (int i = 0; i < listAux.size(); i++) {
                        adapter.addListItem(listAux.get(i), mList.size());
                    }
                }
            }
        });

        GridLayoutManager llm = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(llm);

        CarAdapter adapter = new CarAdapter(getActivity(), mList);
        adapter.setRecyclerViewOnClickListenerHack(this);
        mRecyclerView.setAdapter(adapter);

        setFloatingActionButton(view);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
