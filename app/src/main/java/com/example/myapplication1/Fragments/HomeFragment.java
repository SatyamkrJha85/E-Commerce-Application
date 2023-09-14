package com.example.myapplication1.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.myapplication1.Activities.ShowAllActivity;
import com.example.myapplication1.Adapters.CategoryAdapter;
import com.example.myapplication1.Adapters.NewProductsAdapter;
import com.example.myapplication1.Adapters.PopularProductAdapter;
import com.example.myapplication1.Models.CategoryModel;
import com.example.myapplication1.Models.NewProductsModel;
import com.example.myapplication1.Models.PopularProductModel;
import com.example.myapplication1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    TextView catShowAll,popularShowAll,newProductShowAll;

    ProgressDialog progressDialog;

    LinearLayout linearLayout;

    // Category Recycler View

    RecyclerView catRecyclerview,newProductRecyclerview,popularRecyclerview;

    CategoryAdapter categoryAdapter;
    List<CategoryModel> categoryModelList;

    // new product recycler View

    NewProductsAdapter newProductsAdapter;
    List<NewProductsModel> newProductsModelList;

    // Popular Product Recycler View

    PopularProductAdapter popularProductAdapter;
    List<PopularProductModel> popularProductModelList;

    // Firebase

    FirebaseFirestore db;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_home, container, false);
        db=FirebaseFirestore.getInstance();


        //categories



        progressDialog=new ProgressDialog(getActivity());
        catRecyclerview=root.findViewById(R.id.rec_category);
        newProductRecyclerview=root.findViewById(R.id.new_product_rec);
        popularRecyclerview=root.findViewById(R.id.popular_rec);
        linearLayout=root.findViewById(R.id.home_layout);
        catShowAll=root.findViewById(R.id.category_see_all);
        popularShowAll=root.findViewById(R.id.popular_see_all);
        newProductShowAll=root.findViewById(R.id.newProducts_see_all);

        catShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), ShowAllActivity.class);
                startActivity(intent);
            }
        });
        popularShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), ShowAllActivity.class);
                startActivity(intent);
            }
        });
        newProductShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), ShowAllActivity.class);
                startActivity(intent);
            }
        });


        linearLayout.setVisibility(View.GONE);


        // Image Slider
        ImageSlider imageSlider=root.findViewById(R.id.image_slider);
        List<SlideModel>slideModels=new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.shoes,"Discount On shoes Item", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.perfumes,"Discount On Perfumes", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.clothes,"Discount On Clothes", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.discount,"70% OFF", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.phones,"New Offers on SmartPhones", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.headphones,"Get your best headphones", ScaleTypes.CENTER_CROP));

        imageSlider.setImageList(slideModels,ScaleTypes.FIT);

        progressDialog.setTitle("Welcome To Our Ecommece Application");
        progressDialog.setMessage("Please Wait....");
        progressDialog.setCanceledOnTouchOutside(false);
        linearLayout.setVisibility(View.VISIBLE);
        progressDialog.show();

        // Caregories
        catRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));

        categoryModelList= new ArrayList<>();
        categoryAdapter=new CategoryAdapter(getContext(),categoryModelList);
        catRecyclerview.setAdapter(categoryAdapter);


        // FireStore implementation for category

        db.collection("Category")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                CategoryModel categoryModel=document.toObject(CategoryModel.class);
                                categoryModelList.add(categoryModel);
                                categoryAdapter.notifyDataSetChanged();
                                progressDialog.dismiss();
                            }
                        }
                        else{

                            Toast.makeText(getActivity(), "not set"+task.getException(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

        // New Product
        newProductRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        newProductsModelList=new ArrayList<>();
        newProductsAdapter=new NewProductsAdapter(getContext(),newProductsModelList);
        newProductRecyclerview.setAdapter(newProductsAdapter);

        // FireStore implementation for new product

        db.collection("NewProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){

                                NewProductsModel newProductsModel=document.toObject(NewProductsModel.class);
                                newProductsModelList.add(newProductsModel);
                                newProductsAdapter.notifyDataSetChanged();
                            }
                        }
                        else{

                            Toast.makeText(getActivity(), "not set"+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        // Popular Category

        popularRecyclerview.setLayoutManager(new GridLayoutManager(getContext(),2));
        popularProductModelList=new ArrayList<>();
        popularProductAdapter=new PopularProductAdapter(getContext(),popularProductModelList);
        popularRecyclerview.setAdapter(popularProductAdapter);

        db.collection("AllProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){

                                PopularProductModel popularProductModel=document.toObject(PopularProductModel.class);
                                popularProductModelList.add(popularProductModel);
                                popularProductAdapter.notifyDataSetChanged();
                            }
                        }
                        else{

                            Toast.makeText(getActivity(), "not set"+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });





        return root;
    }
}