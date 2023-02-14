package com.example.doannhom11;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.function.Function;

public class Product {
    private String masp,tensp;
    private int gia;

    public Product() {
    }

    public Product(String masp, String tensp, int gia) {
        this.masp = masp;
        this.tensp = tensp;
        this.gia = gia;
    }
    public String getMasp() {
        return masp;
    }

    public void setMasp(String masp) {
        this.masp = masp;
    }

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }
}
class ProductAdapter extends BaseAdapter implements Filterable
{
    TextView tvname,tvmasp,tvprice;
    ImageView ava;
    private Context m_Context;
    private ArrayList<Product> m_array,temp_array;
    private int m_Layout;
    CustomFilter cs;
    public ProductAdapter(Context context, int layout, ArrayList<Product> arrayList)
    {
        m_Context = context;
        m_Layout = layout;
        m_array = arrayList;
        temp_array = arrayList;
    }
    @Override
    public int getCount() {
        return m_array.size();
    }

    @Override
    public Object getItem(int i) {
        return m_array.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(m_Context).inflate(m_Layout,null);
        tvname = (TextView) view.findViewById(R.id.tvname);
        tvmasp = (TextView) view.findViewById(R.id.tvmasp);
        tvprice = (TextView) view.findViewById(R.id.tvprice);
        ava = (ImageView) view.findViewById(R.id.imageDrink);

        tvname.setText(m_array.get(i).getTensp());
        tvprice.setText(m_array.get(i).getGia()+" đ"); // gia'
        tvmasp.setText("");
        ImageLoader.Load("/images/goods/" + m_array.get(i).getMasp() + ".jpg", ava);
        return view;
    }
    @Override
    public Filter getFilter() {
        if (cs==null)
        {
            cs = new CustomFilter();
        }
        return cs;
    }
    class CustomFilter extends Filter
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint!= null &&  constraint.length()>0)
            {
                constraint = constraint.toString().toUpperCase();
                ArrayList<Product> filters = new ArrayList<>(); // bộ lọc

                for (int i=0 ; i<temp_array.size();i++)
                {
                    if (temp_array.get(i).getTensp().toUpperCase().contains(constraint))
                    {
                        Product product = new Product(temp_array.get(i).getMasp(),
                                temp_array.get(i).getTensp(),temp_array.get(i).getGia());
                        filters.add(product);
                    }
                    results.count = filters.size();
                    results.values = filters;
                }
            }
            else
            {
                results.count = temp_array.size();
                results.values = temp_array;
            }
            return results;
        }
        @Override
        protected void publishResults(CharSequence charSequence,FilterResults filterResults )
        {
            m_array = (ArrayList<Product>) filterResults.values;
            notifyDataSetChanged();
        }
    }
}
// Dùng cho layout có thể update data
class ProductAdapterUpdate extends BaseAdapter implements Filterable
{
    TextView tvnameupdate, tvmaspupdate, tvpriceupdate;
    ImageView avaupdate;
    private Context m_Context;
    private ArrayList<Product> m_array,temp_array;
    private int m_Layout;

    CustomFilter cs;

    public ProductAdapterUpdate(Context context, int layout, ArrayList<Product> arrayList) {
        m_Context = context;
        m_Layout = layout;
        m_array = arrayList;
        temp_array = arrayList;
    }

    @Override
    public int getCount() {
        return m_array.size();
    }

    @Override
    public Object getItem(int i) {
        return m_array.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(m_Context).inflate(m_Layout, null);
        tvnameupdate = (TextView) view.findViewById(R.id.tvnameupdate);
        tvmaspupdate = (TextView) view.findViewById(R.id.tvmaspupdate);
        tvpriceupdate = (TextView) view.findViewById(R.id.tvpriceupdate);
        avaupdate = (ImageView) view.findViewById(R.id.imageDrinkupdate);

        tvnameupdate.setText(m_array.get(i).getTensp());
        tvpriceupdate.setText("" + m_array.get(i).getGia() + " đ"); // giá
        tvmaspupdate.setText("");

        ImageLoader.Load( "/images/goods/"  + m_array.get(i).getMasp() + ".jpg", avaupdate);
        return view;
    }

    @Override
    public Filter getFilter() {
        if (cs==null)
        {
            cs = new CustomFilter();
        }
        return cs;
    }
    class CustomFilter extends Filter
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint!= null &&  constraint.length()>0)
            {
                constraint = constraint.toString().toUpperCase();
                ArrayList<Product> filters = new ArrayList<>(); // bộ lọc

                for (int i=0 ; i<temp_array.size();i++)
                {
                    if (temp_array.get(i).getTensp().toUpperCase().contains(constraint))
                    {
                        Product product = new Product(temp_array.get(i).getMasp(),
                                temp_array.get(i).getTensp(),temp_array.get(i).getGia());
                        filters.add(product);
                    }
                    results.count = filters.size();
                    results.values = filters;
                }
            }
            else
            {
                results.count = temp_array.size();
                results.values = temp_array;
            }
            return results;
        }
        @Override
        protected void publishResults(CharSequence charSequence,FilterResults filterResults )
        {
            m_array = (ArrayList<Product>) filterResults.values;
            notifyDataSetChanged();
        }
    }
}