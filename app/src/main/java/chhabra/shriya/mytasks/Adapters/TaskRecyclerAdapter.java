package chhabra.shriya.mytasks.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import chhabra.shriya.mytasks.MapsActivity;
import chhabra.shriya.mytasks.Models.Task;
import chhabra.shriya.mytasks.R;

/**
 * Created by LENOVO on 1/10/2018.
 */

public class TaskRecyclerAdapter extends EmptyRecyclerView.Adapter<TaskRecyclerAdapter.CourseViewHolder>{
    private Context context;
    private ArrayList<Task> tasks;
    private View.OnClickListener ocl;

    public TaskRecyclerAdapter(final Context context,final ArrayList<Task> task,final RecyclerView recyclerView) {
        this.context=context;
        this.tasks=task;
        ocl = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int pos = recyclerView.getChildAdapterPosition(view);
                Intent i = new Intent(context,MapsActivity.class);
                i.putExtra("name",tasks.get(pos).getTaskName());
                context.startActivity(i);
            }
        };
    }

    public void setArray(ArrayList<Task> tasks){
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = li.inflate(viewType, parent, false);
        itemView.setOnClickListener(ocl);
        return new CourseViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.task_item;
    }

    @Override
    public void onBindViewHolder(CourseViewHolder holder, int position) {
         Task currtask=tasks.get(position);
         holder.TaskName.setText(currtask.getTaskName());
         Log.d("TAG",currtask.getTaskName()+" "+currtask.isDone());
         if(currtask.isDone())
             holder.TaskName.setPaintFlags(holder.TaskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.TaskPlace.setText(currtask.getPlaceAddress());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }


    public class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView TaskName,TaskPlace;

        public CourseViewHolder(View itemView) {
            super(itemView);
            TaskName=itemView.findViewById(R.id.task_item_name);
            TaskPlace=itemView.findViewById(R.id.task_item_place);
        }
    }
}
