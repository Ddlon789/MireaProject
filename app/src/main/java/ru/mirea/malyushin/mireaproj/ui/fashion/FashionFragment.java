package ru.mirea.malyushin.mireaproj.ui.fashion;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.malyushin.mireaproj.databinding.FragmentFashionBinding;

public class FashionFragment extends Fragment {

    private FragmentFashionBinding binding;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
                String currentTime = (String) message.obj;
                binding.time.setText(currentTime);
                return true;
        }
    });

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        TimeWorker timeWorker = new TimeWorker(handler);
        timeWorker.start();

        FashionViewModel fashionViewModel = new ViewModelProvider(this).get(FashionViewModel.class);

        binding = FragmentFashionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        final TextView textView = binding.text1;
        final TextView textView2 = binding.text2;
        final TextView textView3 = binding.text3;
        final TextView textView4 = binding.textView4;

        textView.setText("Как сохранить люкс и премиум в России? Новое видение кооперации — типизация");

        textView2.setText("Коллаборация/парнерство - это один из основополагающих способов развития  портфельных стратегий и модульного проектирования, являющихся спасательным кругом в такие, турбулентные, времена. Но иногда лучше один раз показать, чем семь раз объяснить.\n" +
                "Начнем с механизма fashion-индустрии, работающего в мире.");

        textView3.setText("В общих чертах это работает так и у нас. Отличие лишь в том, что начало и конец деятельности, каждая организация определяет сама - нет четкого распределения обязанностей между звеньями всей цепочки.\n" +
                "\n" +
                "Визуализируя дерево взаимосвязей в динамике, всё будет представлено в виде шума, но отрисовав в статике, можно заметить турбулентность потоков, связанного, по большей части с отсутствием кооперации.");

        textView4.setText("Последовательное, а не параллельное выполнение задач способствует увеличение рисков на каждом последующем производственном этапе. Эта передача рисков схожа с игрой в горячую картошку." +
                " Один и тот же вариант распределения не подойдет нескольким брендам, не из-за их разности концепций или процентной доли рынка, а из-за их разности в проценте охвата составляющих производственного цикла.");
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}