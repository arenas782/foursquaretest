package com.example.foursquaretest.utils

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.api.load
import coil.transform.CircleCropTransformation
import com.example.foursquaretest.App
import com.example.foursquaretest.R
import com.example.foursquaretest.data.model.responses.Category
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.math.RoundingMode


@BindingAdapter("adapter")
fun setAdapter(rv: RecyclerView, mAdapter: RecyclerView.Adapter<*>) {
    rv.apply {
        setHasFixedSize(true)
        adapter = mAdapter
    }
}

@BindingAdapter("categoryIcon")
fun categoryIcon(iv : ImageView, categories : List<Category?>?) {

    categories?.forEach {
        if (it != null) {
            val url = it.icon?.prefix + "44" + it.icon?.suffix

            val circularProgressDrawable = CircularProgressDrawable(App.appContext)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()
            MainScope().launch {

                iv.load(url){
                    crossfade(true)
                    placeholder(circularProgressDrawable)
                    transformations(CircleCropTransformation())
                }

            }
            return@forEach
        }
    }
}

@BindingAdapter("categoryName")
fun categoryName(tv : TextView, categories : List<Category?>?) {
    categories?.forEach {
        if (it!=null) {
            tv.text = it.name
            return@forEach
        }
    }
}
@BindingAdapter("distance")
fun distance(tv : TextView, distance : Double) {
    //set meters or kilometers for distance on text
    val rounded = (distance/1000).toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
    if (rounded <1){
        val roundedMeters =distance.toBigDecimal().setScale(1,RoundingMode.UP).toInt()
        tv.text = Commons.getString(R.string.distance_m_placeholder,roundedMeters.toString())
    }else{
        tv.text = Commons.getString(R.string.distance_km_placeholder,rounded.toString())
    }
}





