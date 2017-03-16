package com.stockhawk.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.stockhawk.R;
import com.stockhawk.data.ItemDetails;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

public class DetailsActivity extends AppCompatActivity {

    public GraphView graph;

    public ItemDetails itemDetails ;
    public List <GraphData> graphDatasList = new ArrayList<>();
    Double maxYAxis ;
    Double minYAxis ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        graph = (GraphView) findViewById(R.id.graph);
        if (getIntent().getExtras()!=null)
        {
            itemDetails = getIntent().getExtras().getParcelable("itemSelected");

        }

        getGraphData(itemDetails.history);
        setGraph ();

    }

    public void setGraph ()
    {
        int listSize = graphDatasList.size();


        DataPoint[] values = new DataPoint[listSize];
        for (int i = 0 ; i< listSize ;i++)
        {
            values[i] =new DataPoint( graphDatasList.get(i).dayDate ,graphDatasList.get(i).value );
        }
        LineGraphSeries <DataPoint> series = new LineGraphSeries<>(values);
        graph.addSeries(series);

        graph.getViewport().setScrollable(true);
        graph.getViewport().setScrollableY(true);

        graph.getViewport().setYAxisBoundsManual(true);
        Double []maxAndMin = getMaxAndMin (graphDatasList);
        maxYAxis = maxAndMin[0];
        minYAxis =  maxAndMin[1];
        graph.getViewport().setMinY(minYAxis);
        graph.getViewport().setMaxY(maxYAxis);

        Log.e("min" ,String.valueOf(minYAxis));
        Log.e("max" ,String.valueOf(maxYAxis));
        // set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
        graph.getGridLabelRenderer().setNumHorizontalLabels(listSize);

        Date d1 = graphDatasList.get(0).dayDate ;
        Date d3 = graphDatasList.get((listSize-1)).dayDate ;

        // set manual x bounds to have nice steps
        graph.getViewport().setMinX(d1.getTime() );
        graph.getViewport().setMaxX(d3.getTime());
        graph.getViewport().setXAxisBoundsManual(true);

       // as we use dates as labels, the human rounding to nice readable numbers
          // is not necessary
        graph.getGridLabelRenderer().setHumanRounding(false);
    }


    public void getGraphData (String history) {

        String[] allHistory = history.split("\n");
        for (int i = 0; i < allHistory.length; i++) {
            GraphData graphData = new GraphData();
            String[] dayHistory = allHistory[i].split(",");
            Date date = parseHistory(dayHistory[0]);
            if (date==null)
            {
                continue;
            }
            graphData.dayDate =date;
            graphData.value =Double.valueOf(dayHistory[1]);
            graphDatasList.add(graphData);
        }

        Collections.sort(graphDatasList , new GraphData());
    }

    public Date parseHistory (String milliSecondsString){
        long milliSeconds = Long.valueOf(milliSecondsString);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        int mYear =  calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int currentMonth= Calendar.getInstance().get(Calendar.MONTH);

        Date date = calendar.getTime() ;
        if (mYear < currentYear )
        {
            return null;
        }
        return  date ;
                /*

        int mMonth = calendar.get(Calendar.MONTH);
        int mDay =   calendar.get(Calendar.DAY_OF_MONTH);*/


    }


    public class GraphData  implements Comparator<GraphData> {
        public Date  dayDate  ;
        public Double value ;

        @Override
        public int compare(GraphData o1, GraphData o2) {
            return o1.dayDate.compareTo(o2.dayDate);
        }

    }

    public  Double [] getMaxAndMin (List<GraphData> graphDatas)
    {
        Double max = graphDatas.get(0).value;
        Double min = graphDatas.get(0).value;
        for (int  i=1;i<graphDatas.size() ;i++)
        {
            GraphData graphDa = new GraphData();
            graphDa =graphDatas.get(i);
            if(graphDa.value>max)
            {
                max =graphDa.value;
            }
            if(graphDa.value<min)
            {
                min =graphDa.value;
            }
        }
        Double []result = {max ,min};
        return result ;
    }

}


