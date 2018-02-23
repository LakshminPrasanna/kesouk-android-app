package com.tstl.kesouk.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.tstl.kesouk.Activity.Login_Register_Activity;
import com.tstl.kesouk.Activity.Navigation_Tab_Activity;
import com.tstl.kesouk.Adapter.Category_Adapter;
import com.tstl.kesouk.DB.DB;
import com.tstl.kesouk.Model.Browse_Category;
import com.tstl.kesouk.Model.Constants;
import com.tstl.kesouk.Model.Recipe;
import com.tstl.kesouk.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.tstl.kesouk.Activity.TabMain_Activity.search;
import static com.tstl.kesouk.Activity.TabMain_Activity.toolbar_title;

/**
 * Created by user on 01-Feb-18.
 */

public class Product_Description_Fragment extends Fragment implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener {
    View view, tab1, tab2, tab3;
    private Typeface mDynoRegular;
    //private Toolbar mToolbar;
    DB db;
    //ImageView img_back, settings, search;
   // private TextView mToolbarTitle;
    TextView mProductName, mAmount, mRatingText, description_title, nutrition_title, supplier_information, textView, similar_products;
    Button mAddtoCart;
    ImageView mExpress;
    ToggleButton mFav;
    SliderLayout slider;
    RatingBar ratingBar;
    RecyclerView similar_products_recylerview;
    String product_random_id;
    int category_id, is_express_delivery;
    private ArrayList<String> imageslidinglist = new ArrayList<>();
    String product_name, product_image, product_price1, nutrition_info, product_image_array, description, similar_id, similar_category, similar_subcategory,qty_name,search_word,amount,spinner_quantity;
    TextSliderView textSliderView;
    String product_price_id="",price_product_id="";
    int product_id=1;


    public ArrayList<Browse_Category> list_browse_products, list_similar_products;

    public static Product_Description_Fragment newInstance() {
        Product_Description_Fragment fragment = new Product_Description_Fragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.product_desciption_layout, container, false);


        mProductName = (TextView) view.findViewById(R.id.tv_prodname);
        mAmount = (TextView) view.findViewById(R.id.tv_amount);
        mRatingText = (TextView) view.findViewById(R.id.rating_text);
        description_title = (TextView) view.findViewById(R.id.desc_title);
        nutrition_title = (TextView) view.findViewById(R.id.nutri_info);
        supplier_information = (TextView) view.findViewById(R.id.supplr_info);
        textView = (TextView) view.findViewById(R.id.textview);
        similar_products = (TextView) view.findViewById(R.id.similar);
        tab1 = (View) view.findViewById(R.id.tab_view1);
        tab2 = (View) view.findViewById(R.id.tab_view2);
        tab3 = (View) view.findViewById(R.id.tab_view3);
        mAddtoCart = (Button) view.findViewById(R.id.add_to_cart);
        mExpress = (ImageView) view.findViewById(R.id.bike);
        mFav = (ToggleButton) view.findViewById(R.id.fav);
        slider = (SliderLayout) view.findViewById(R.id.slider);
        ratingBar = (RatingBar) view.findViewById(R.id.ratingbar);
        similar_products_recylerview = (RecyclerView) view.findViewById(R.id.list_similar_products);


        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
      /*  mToolbar = (Toolbar) view.findViewById(R.id.logintoolbar);
        img_back = (ImageView) view.findViewById(R.id.img);
        settings = (ImageView) view.findViewById(R.id.settings);
        search = (ImageView) view.findViewById(R.id.search);
        settings.setVisibility(View.GONE);
        search.setVisibility(View.GONE);
*/

        db = new DB(getActivity());

     /*   mToolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);

        mToolbarTitle.setVisibility(View.VISIBLE);
        mToolbarTitle.setText("Product Info");

        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);*/
        setFont();
        search.setVisibility(View.GONE);
        toolbar_title.setVisibility(View.VISIBLE);
        toolbar_title.setText("Product Info");

        Bundle bundle1 = this.getArguments();
        if (bundle1 != null) {

            category_id = bundle1.getInt("position");
            product_random_id = bundle1.getString("product_random_id");
            similar_category = bundle1.getString("category");
            similar_subcategory = bundle1.getString("subcategory");
            similar_id = bundle1.getString("id");
            qty_name = bundle1.getString("qty_name");
            amount = bundle1.getString("price");
            spinner_quantity = bundle1.getString("qty");
            search_word = bundle1.getString("search_word");
            similar_products.setText("SIMILAR "+ search_word);
            product_id = bundle1.getInt("product_id");
            product_price_id = bundle1.getString("product_price_id");
            price_product_id = bundle1.getString("price_product_id");


        }
        getProductDescription();
        getSimilarProducts();

/*
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (db.getAllLogin().size() == 1) {


                    if (getFragmentManager().getBackStackEntryCount() > 0) {
                        getFragmentManager().popBackStack();
                    } else {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        getActivity().finish();
                        System.exit(0);
                    }

                } else {
                    if (getFragmentManager().getBackStackEntryCount() > 0) {
                        getFragmentManager().popBackStack();
                    } else {
                        Intent intent = new Intent(getActivity(), Login_Register_Activity.class);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        getActivity().finish();
                    }

                }

            }
        });*/
        mAddtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                Map<String, String> params = new HashMap<String, String>();
                Log.e("customer_id ","0");
                Log.e("product_id ", String.valueOf(product_id));
                Log.e("quantity ","1");
                Log.e("price_id ", String.valueOf(price_product_id));

                if(db.getAllLogin().size()==1)
                {
                    params.put("customer_id", db.getAllLogin().get(0));

                }
                else if(db.getAllLogin().size()==0)
                {
                    params.put("customer_id", "0");

                }

                params.put("product_id", String.valueOf((product_id)));
                params.put("quantity", "1");
                params.put("price_id", String.valueOf(price_product_id));




                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        Constants.ADDTOCART, new JSONObject(params),
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject object) {
                                try {
                                    Log.e("addtocart", String.valueOf(object));


                                    String status=object.getString("status");
                                    String  data_id = object.getString("data");
                                    Log.e("data_id",data_id);

                                    if(status.equals("Success"))
                                    {
                                        if(db.getAllLogin().size()==1)
                                        {

                                        }
                                        else if(db.getAllLogin().size()==0)
                                        {
                                            db.insert(data_id);
                                            Log.e("fetch", String.valueOf(db.getAllData()));
                                        }


                                        AlertDialog.Builder builder =
                                                new AlertDialog.Builder(getActivity());
                                        builder.setTitle("Message");
                                        builder.setMessage(product_name.toUpperCase() +" has been added to Basket !");

                                        String positiveText = "Ok";
                                        builder.setPositiveButton(positiveText,
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });

                                        String negativeText = getActivity().getString(android.R.string.cancel);
                                        builder.setNegativeButton(negativeText,
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // negative button logic
                                                        dialog.dismiss();
                                                    }
                                                });
                                        AlertDialog dialog = builder.create();
                                        // display dialog
                                        dialog.show();
                                    }
                                    else
                                    {
                                        String reason=object.getString("reason");
                                        Toast.makeText(getActivity(), reason ,Toast.LENGTH_LONG).show();

                                    }


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        Log.e("verify_otp_error", "error" + volleyError);
                        if (volleyError instanceof TimeoutError) {
                            Toast.makeText(getActivity(), "Connection was timeout. Please check your internet connection ", Toast.LENGTH_LONG).show();
                        }
                        else
                            Toast.makeText(getActivity(), "Please check your internet connection or server is not connected", Toast.LENGTH_LONG).show();

                        VolleyLog.d("responseError", "Error: " + volleyError);

                    }
                }) {



                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<>();


                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> header = new HashMap<String, String>();
                        header.put("Content-Type", "application/json; charset=utf-8");
                        return header;
                    }

                };
                jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                        Constants.MY_SOCKET_TIMEOUT_MS,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                queue.add(jsonObjReq);


            }
        });


        mFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity(),qty_name+ " has been added to your Wishlist", Toast.LENGTH_LONG).show();

                RequestQueue queue = Volley.newRequestQueue(getActivity());
                Map<String, String> params = new HashMap<String, String>();



                if(db.getAllLogin().size()==1)
                {
                    params.put("customer_id", db.getAllLogin().get(0));

                }
                else if(db.getAllLogin().size()==0)
                {
                    params.put("customer_id", "0");

                }else if(db.getAllLogin().size()==2)
                {
                    params.put("customer_id", db.getAllLogin().get(1));

                }

                params.put("product_id", String.valueOf(product_id));
                params.put("price_id",product_price_id );





                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        Constants.ADDTOWISHLIST, new JSONObject(params),
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject object) {
                                try {
                                    Log.e("wishlist", String.valueOf(object));


                                    String status=object.getString("status");
                                    if(status.equals("Success"))
                                    {
                                        String  data_id = object.getString("data");
                                        Log.e("data_id",data_id);

                                        if(db.getAllLogin().size()==1)
                                        {

                                        }
                                        else if(db.getAllLogin().size()==0)
                                        {
                                            db.insert_wishlist(data_id);
                                            Log.e("fetch", String.valueOf(db.getAllWishlist()));
                                        }
                                    }
                                    else
                                    {
                                        String reason=object.getString("reason");
                                        Toast.makeText(getActivity(), reason ,Toast.LENGTH_LONG).show();

                                    }


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        Log.e("verify_otp_error", "error" + volleyError);
                        if (volleyError instanceof TimeoutError) {
                            Toast.makeText(getActivity(), "Connection was timeout. Please check your internet connection ", Toast.LENGTH_LONG).show();
                        }
                        else
                            Toast.makeText(getActivity(), "Please check your internet connection or server is not connected", Toast.LENGTH_LONG).show();

                        VolleyLog.d("responseError", "Error: " + volleyError);

                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<>();


                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> header = new HashMap<String, String>();
                        header.put("Content-Type", "application/json; charset=utf-8");
                        return header;
                    }

                };
                jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                        Constants.MY_SOCKET_TIMEOUT_MS,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                queue.add(jsonObjReq);


            }
        });
        return view;
    }

    private void getProductDescription() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        Map<String, String> params = new HashMap<String, String>();


        params.put("product_id", product_random_id);
        params.put("wishcheck", "0");
        params.put("localwish", "0");
        params.put("cartcheck", "0");
        params.put("customer_id", "0");
        params.put("localcart", "0");
        Log.e("json", String.valueOf(params));


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.GET_PRODUCT_DETAILS, new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject object) {
                        try {
                            Log.e("product_detail", String.valueOf(object));


                            String status = object.getString("status");
                            if (status.equals("Success")) {

                                JSONObject JObject = new JSONObject(String.valueOf(object));
                                JSONArray DataArray = JObject.getJSONArray("data");

                                Log.e("data", "data");
                                Browse_Category browse_category = null;
                                if (DataArray.length() != 0) {
                                    list_browse_products = new ArrayList<>();
                                    for (int i = 0; i < DataArray.length(); i++) {

                                        Log.e("datacount", String.valueOf(DataArray.length()));
                                        JSONObject jsonObject = DataArray.getJSONObject(i);
                                        product_name = jsonObject.getString("product_name");
                                        is_express_delivery = jsonObject.getInt("is_express_delivery");
                                        description = jsonObject.getString("description");
                                        nutrition_info = jsonObject.getString("nutrition_info");

                                     /*   similar_category = jsonObject.getString("category");
                                        similar_subcategory = jsonObject.getString("sub_category");
                                        similar_id = jsonObject.getString("id");*/
                                        textView.setText(description);
                                        tab1.setVisibility(View.VISIBLE);
                                        tab2.setVisibility(View.GONE);
                                        tab3.setVisibility(View.GONE);
                                        int id = jsonObject.getInt("id");
                                        float rating = jsonObject.getInt("rating_star");

                                        browse_category = new Browse_Category();

                                        browse_category.setProduct_desc(description);
                                        browse_category.setProduct_id(id);
                                        browse_category.setRatingStar(rating);
                                        browse_category.setIs_express_delivery(is_express_delivery);


                                        JSONArray product_images = jsonObject.getJSONArray("product_images");
                                        //list_browse_products.add(id);
                                        HashMap<String, String> file_maps = new HashMap<String, String>();
                                        if (product_images.length() != 0) {
                                            for (int j = 0; j < product_images.length(); j++) {
                                                Log.e("datacount", String.valueOf(product_images.length()));
                                                JSONObject jsonObject1 = product_images.getJSONObject(j);
                                                product_image_array = jsonObject1.getString("product_image");
                                                Log.e("product_image_array", product_image_array);
                                                imageslidinglist.add(product_image_array);
                                                file_maps.put(String.valueOf(j), Constants.PRODUCT_IMAGES + product_image_array);

                                            }
                                            browse_category.setProduct_sliding(imageslidinglist);
                                        } else {
                                            HashMap<String, Integer> file_maps1 = new HashMap<String, Integer>();
                                            file_maps1.put("1", R.drawable.logo);

                                        }

                                        mProductName.setText(product_name+ " -"+spinner_quantity);
                                        mAmount.setText(amount);
                                        if (is_express_delivery == 1) {
                                            mExpress.setVisibility(View.VISIBLE);
                                        } else {
                                            mExpress.setVisibility(View.GONE);

                                        }


                                        //ratingBar.setRating(rating);
                                        Log.e("kesouk", description);
                                        // Glide.with(Product_Details_Activity.this).load(Constants.PRODUCT_IMAGES + browse_category.getProduct_image()).placeholder(R.drawable.dukan_logo).into(mProduct_image);

                                        for (String name : file_maps.keySet()) {
                                            textSliderView = new TextSliderView(getActivity());
                                            // initialize a SliderLayout
                                            textSliderView
                                                    .image(file_maps.get(name))
                                                    .setScaleType(BaseSliderView.ScaleType.Fit)
                                                    .setOnSliderClickListener(Product_Description_Fragment.this);

                                            //add your extra information
                                            textSliderView.bundle(new Bundle());
                                            textSliderView.getBundle()
                                                    .putString("extra", name);


                                            slider.addSlider(textSliderView);
                                            slider.setPresetTransformer(SliderLayout.Transformer.Accordion);
                                            //imageSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                                            slider.setCustomAnimation(new DescriptionAnimation());
                                            slider.setDuration(2000);
                                            slider.addOnPageChangeListener(Product_Description_Fragment.this);


                                        }


                                        description_title.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                textView.setText(description);
                                                tab1.setVisibility(View.VISIBLE);
                                                tab2.setVisibility(View.GONE);
                                                tab3.setVisibility(View.GONE);

                                            }
                                        });
                                        nutrition_title.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                textView.setText(nutrition_info);
                                                tab2.setVisibility(View.VISIBLE);
                                                tab1.setVisibility(View.GONE);
                                                tab3.setVisibility(View.GONE);

                                            }
                                        });
                                        supplier_information.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                textView.setText(description);
                                                tab3.setVisibility(View.VISIBLE);
                                                tab2.setVisibility(View.GONE);
                                                tab1.setVisibility(View.GONE);

                                            }
                                        });


                                        Log.e("product_name", browse_category.getProd_name());
                                        Log.e("product_image", browse_category.getProduct_image());
                                        Log.e("product_price", browse_category.getDukanPrice());
                                        Log.e("id", String.valueOf(id));

                                    }


                                }


                            } else {
                                String reason = object.getString("reason");
                                Toast.makeText(getActivity(), reason, Toast.LENGTH_LONG).show();

                            }

                            // setAdapter();


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Log.e("verify_otp_error", "error" + volleyError);
                if (volleyError instanceof TimeoutError) {
                    Toast.makeText(getActivity(), "Connection was timeout. Please check your internet connection ", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(getApplicationContext(), "Please check your internet connection or server is not connected", Toast.LENGTH_LONG).show();

                VolleyLog.d("responseError", "Error: " + volleyError);

            }
        }) {

          /*  @Override
            public String getBodyContentType() {
                return "application/json; charset=UTF-8";


            }*/

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();


                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<String, String>();
                header.put("Content-Type", "application/json; charset=utf-8");
                return header;
            }

        };
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                Constants.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjReq);

    }

    private void setFont() {

        mDynoRegular = Typeface.createFromAsset(getActivity().getAssets(),
                "font/Roboto_Regular.ttf");

        mProductName.setTypeface(mDynoRegular);
        mAmount.setTypeface(mDynoRegular);
        mRatingText.setTypeface(mDynoRegular);
        description_title.setTypeface(mDynoRegular);
        nutrition_title.setTypeface(mDynoRegular);
        supplier_information.setTypeface(mDynoRegular);
        textView.setTypeface(mDynoRegular);
        similar_products.setTypeface(mDynoRegular);
        mAddtoCart.setTypeface(mDynoRegular);

    }


    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public class SimilarAdapter extends RecyclerView.Adapter<SimilarAdapter.SingleItemRowHolder> {

        private ArrayList<Browse_Category> itemsList;
        private Context mContext;
        private String transaction;


        public SimilarAdapter(Context context, ArrayList<Browse_Category> itemsList) {
            this.itemsList = itemsList;
            this.mContext = context;

        }

        @Override
        public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_list_item, null);

            return new SingleItemRowHolder(v);
        }

        @Override
        public void onBindViewHolder(final SingleItemRowHolder holder, final int i) {

            final Browse_Category browse_category = itemsList.get(i);

            holder.tvTitle.setText(browse_category.getProd_name());
            Glide.with(mContext)
                    .load(Constants.SIMILAR_PRODUCTS_IMAGES + browse_category.getProduct_image())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                   .centerCrop()
                    .error(R.drawable.logo)
                    .into(holder.itemImage);

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                 /*   Recipe_category_list_Fragment newFragment = new Recipe_category_list_Fragment();
                    Bundle args = new Bundle();
                    args.putInt("position", recipe.getRecipeTypeId());
                    newFragment.setArguments(args);

                    FragmentTransaction transaction = ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.container_fragment,newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();*/



              /*  SingleItemModel singleItemModel = itemsList.get(i);
                singleItemModel.getName();

                Intent intent = new Intent(mContext.getApplicationContext(),Browse_Category_Activity.class);



//Create the bundle
                Bundle bundle = new Bundle();
//Add your data from getFactualResults method to bundle
                bundle.putInt("category_selected_name", groupPosition);
//Add the bundle to the intent
                intent.putExtras(bundle);

                mContext.startActivity(intent);
*/

                }
            });
        }

        @Override
        public int getItemCount() {
            return (null != itemsList ? itemsList.size() : 0);
        }

        public class SingleItemRowHolder extends RecyclerView.ViewHolder {

            protected TextView tvTitle;

            protected ImageView itemImage;
            private Typeface mDynoRegular;
            CardView cardView;


            public SingleItemRowHolder(View view) {
                super(view);

                this.tvTitle = (TextView) view.findViewById(R.id.products_name);
                this.itemImage = (ImageView) view.findViewById(R.id.image2);
                this.cardView = (CardView) view.findViewById(R.id.cardview);

                mDynoRegular = Typeface.createFromAsset(mContext.getAssets(), "font/Roboto_Regular.ttf");
                tvTitle.setTypeface(mDynoRegular);

            }

        }
    }


    private void getSimilarProducts() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        Map<String, String> params = new HashMap<String, String>();

        params.put("category", similar_category);
        params.put("subcategory", similar_subcategory);
        params.put("id", similar_id);
        params.put("page", "1");
        params.put("per_page", "5");


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.GET_SIMILAR_PRODUCTS, new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject object) {
                        try {
                            Log.e("similar_products", String.valueOf(object));


                            String status = object.getString("status");
                            if (status.equals("Success")) {

                                JSONObject JObject = new JSONObject(String.valueOf(object));
                                JSONObject JObject1 = JObject.getJSONObject("data");
                                JSONArray DataArray = JObject1.getJSONArray("data");
                                Log.e("data", "data");
                                Browse_Category browse_category = null;
                                if (DataArray.length() != 0) {
                                    list_similar_products = new ArrayList<>();
                                    for (int i = 0; i < DataArray.length(); i++) {

                                        Log.e("datacount", String.valueOf(DataArray.length()));
                                        JSONObject jsonObject = DataArray.getJSONObject(i);
                                        product_name = jsonObject.getString("product_name");
                                        product_image = jsonObject.getString("display_image");
                                        product_price1 = jsonObject.getString("price");
                                        String description = jsonObject.getString("description");
                                        int id = jsonObject.getInt("id");


                                        browse_category = new Browse_Category();
                                        browse_category.setProd_name(product_name);
                                        browse_category.setDukanPrice(product_price1);
                                        browse_category.setProduct_image(product_image);
                                        browse_category.setProduct_desc(description);
                                        browse_category.setProduct_id(id);

                                        list_similar_products.add(browse_category);
                                        Log.e("similar_products", String.valueOf(list_similar_products));
                                    }

                                     SimilarAdapter similarAdapter = new SimilarAdapter(getActivity(), list_similar_products);

                                    // similarProducts_recylerview.setHasFixedSize(true);
                                    similar_products_recylerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                                    similar_products_recylerview.setAdapter(similarAdapter);


                                }else
                                {
                                    similar_products.setVisibility(View.GONE);
                                }

                            } else {
                                String reason = object.getString("reason");
                                Toast.makeText(getActivity(), reason, Toast.LENGTH_LONG).show();

                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Log.e("verify_otp_error", "error" + volleyError);
                if (volleyError instanceof TimeoutError) {
                    Toast.makeText(getActivity(), "Connection was timeout. Please check your internet connection ", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(getApplicationContext(), "Please check your internet connection or server is not connected", Toast.LENGTH_LONG).show();

                VolleyLog.d("responseError", "Error: " + volleyError);

            }
        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();


                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<String, String>();
                header.put("Content-Type", "application/json; charset=utf-8");
                return header;
            }

        };
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                Constants.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjReq);

    }

}