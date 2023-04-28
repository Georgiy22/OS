import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Graphics;

public class ThreadManager {
    public static final int GlobalTime = 150; //добавочное временя к задачам, необходимое для более наглядной демонстрации поэтапной работы алгоритма
    public static final long TimeStart = System.currentTimeMillis(); //начально время, от которого берется отсчет
    public static boolean[] flag = {false,false,false}; //аналоги семафор
    public static boolean[] tasks = new boolean[9]; //массив статусов выполнения задач, нужен для их визуализации в реальном времени в интерфейсе
public ThreadManager(int n){
    Thread thr_a = new Thread(new task_a(n)); //вызов задачи А
    thr_a.start();
    Thread thr_draw = new Thread(new task_draw()); //вызов потока, ответственного за создание и обновление интерфейса
    thr_draw.start();
}

public ThreadManager(){} //пустой конструктор, нужен для доступа к методам класса
    private static class task_draw implements Runnable { //поток вывода интерфейса
        public task_draw(){}
        @Override
        public void run() {
            boolean end = true;
            JFrame a = new JFrame("tasks");
            a.setSize(1000,500);
            a.setVisible(true);
            a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            while(end){ //будет работать, пока не завершится последний из процессов
                if(tasks[0]||tasks[1]||tasks[2]||tasks[3]||tasks[4]||tasks[5]||tasks[6]||tasks[7]){
                    a.add(new MyPanel());
                    a.repaint();
                }
                if(tasks[8]){
                    a.add(new MyPanel());
                    a.repaint();
                    end = false;
                }
            }
        }
    }

    public static class MyPanel extends JPanel{ //класс для создания графического интерфейса
        public MyPanel(){setBorder(new LineBorder(Color.RED, 5));}
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            g.drawRect(50, 50, 100, 30);
            g.drawLine(150,65,200,65);
            g.drawLine(150,65,200,100);
            g.drawLine(150,65,200,30);
            g.drawRect(200, 15, 100, 30);
            g.drawRect(200, 50, 100, 30);
            g.drawRect(200, 85, 100, 30);
            g.drawLine(300,65,350,65);
            g.drawLine(300,30,350,65);
            g.drawLine(300,100,350,65);
            g.drawLine(350,65,400,65);
            g.drawLine(350,65,475,100);
            g.drawRect(400, 50, 100, 30);
            g.drawRect(475, 85, 100, 30);
            g.drawLine(500,65,550,65);
            g.drawLine(500,65,550,30);
            g.drawRect(550, 15, 100, 30);
            g.drawRect(550, 50, 100, 30);
            g.drawLine(650,65,700,65);
            g.drawLine(650,30,700,65);
            g.drawLine(575,100,700,65);
            g.drawRect(700, 50, 100, 30);
            if(tasks[0]) {
                g.drawString("Task A complete",55,70);
            }
            if(tasks[1]) {
                g.drawString("Task B complete",205,35);
            }
            if(tasks[2]) {
                g.drawString("Task C complete",205,70);
            }
            if(tasks[3]) {
                g.drawString("Task D complete",205,105);
            }
            if(tasks[4]) {
                g.drawString("Task E complete",405,70);
            }
            if(tasks[5]) {
                g.drawString("Task F complete",480,105);
            }
            if(tasks[6]) {
                g.drawString("Task G complete",555,70);
            }
            if(tasks[7]) {
                g.drawString("Task H complete",555,35);
            }
            if(tasks[8]) {
                g.drawString("Task K complete",705,70);
            }
        }
    }
private static class task_a implements Runnable { //задача А

    public int[][] M;
    public int[] R;
    public int n;
    public task_a(int n){
        this.n = n;
    }

    @Override
    public void run(){
        ThreadManager tm = new ThreadManager(); //для обращения к методам этого класса
        long tn = System.currentTimeMillis() - TimeStart;
        System.out.println("время начала выполнения задачи А: " + tn);
        try {
            Thread.sleep(GlobalTime + ((int) (Math.random() * GlobalTime/2))); //имитация долгой работы функции, для наглядности их переключения, с небольшой вариативностью
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        M = new int[n][n];
        R = new int[n];
        for(int i = 0; i < n; ++i) { //заполнение матрицы и массива случайными числами до 50
            for(int j = 0; j < n; ++j) {
                M[i][j] = (int)(Math.random() * 50);
            }
            R[i] = (int)(Math.random() * 50);
        }
        tn = System.currentTimeMillis() - TimeStart; //определение прошедшего времени с начала работы алгоритма
        System.out.println("результат выполнения задачи А:\n" + "построенная матрица:\n" + tm.toString(M, n) + "построенный массив:\n" + tm.toString(R, n) + "\n" + "время окончания выполнения задачи А: " + tn);
        System.out.println("начинается инициация задачи B, ее инициировала задача A");
        Thread thr_b = new Thread(new task_b(M, R)); //запуски дальнейших потоков
        thr_b.start();
        System.out.println("начинается инициация задачи С, ее инициировала задача А");
        Thread thr_c = new Thread(new task_c(M, R));
        thr_c.start();
        System.out.println("начинается инициация задачи D, ее инициировала задача А");
        Thread thr_d = new Thread(new task_d(M, R));
        thr_d.start();
        tasks[0] = true; //указание для интерфейса о готовности данного процесса
    }
}
private static class task_b implements Runnable {
    public static int[][] M;
    public int[] R;
    public int n;
    public task_b(int[][] Ma, int[] R){
        n = R.length;
        M = new int[n][n];
        for(int i = 0; i < n; ++i) {
            System.arraycopy(Ma[i], 0, M[i], 0, n); //создаются копии матриц, чтобы результат не зависил от случаной очередности выполнения задач
        }
        this.R = R;
    }
    @Override
    public void run(){
        ThreadManager tm = new ThreadManager();
        long tn = System.currentTimeMillis() - TimeStart;
        System.out.println("время начала выполнения задачи B: " + tn);
        try {
            Thread.sleep(GlobalTime + ((int) (Math.random() * GlobalTime/2)));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < n; ++i) { //f1:добавление ко всем элементам матрицы соответствующий их ряду элемент массива
            for(int j = 0; j < n; ++j) {
                M[i][j] += R[i];
            }
        }
        tn = System.currentTimeMillis() - TimeStart;
        System.out.println("результат выполнения задачи B:\nновая матрица:\n" + tm.toString(M, n) + "время окончания выполнения задачи B: " + tn);
        flag[0] = true;
        if (flag[1] && flag[2]){ //проверка на то, является ли этот поток последним из выполнившихся
            System.out.println("начинается инициация задачи E, ее инициировала задача B");
            Thread thr_e = new Thread(new task_e(M,task_c.M,task_d.M));
            thr_e.start();
            System.out.println("начинается инициация задачи F, ее инициировала задача B");
            Thread thr_f = new Thread(new task_f(M,task_c.M,task_d.M));
            thr_f.start();
        }
        tasks[1] = true;
    }
}
    private static class task_c implements Runnable { //аналогичен потоку b
        public static int[][] M;
        public int[] R;
        public int n;
        public task_c(int[][] Ma, int[] R){
            n = R.length;
            M = new int[n][n];
            for(int i = 0; i < n; ++i) {
                System.arraycopy(Ma[i], 0, M[i], 0, n);
            }
            this.R = R;
        }
        @Override
        public void run(){
            ThreadManager tm = new ThreadManager();
            long tn = System.currentTimeMillis() - TimeStart;
            System.out.println("время начала выполнения задачи C: " + tn);
            try {
                Thread.sleep(GlobalTime + ((int) (Math.random() * GlobalTime/2)));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for(int i = 0; i < n; ++i) { //f2:добавление ко всем элементам матрицы соответствующий их колонке элемент массива
                for(int j = 0; j < n; ++j) {
                    M[i][j] += R[j];
                }
            }
            tn = System.currentTimeMillis() - TimeStart;
            System.out.println("результат выполнения задачи C:\nновая матрица:\n" + tm.toString(M, n) + "время окончания выполнения задачи C: " + tn);
            flag[1] = true;
            if (flag[0] && flag[2]){
                System.out.println("начинается инициация задачи E, ее инициировала задача C");
                Thread thr_e = new Thread(new task_e(task_b.M,M,task_d.M));
                thr_e.start();
                System.out.println("начинается инициация задачи F, ее инициировала задача C");
                Thread thr_f = new Thread(new task_f(task_b.M,M,task_d.M));
                thr_f.start();
            }
            tasks[2] = true;
        }
    }
    private static class task_d implements Runnable { //аналогичен потоку b
        public static int[][] M;
        public int[] R;
        public int n;
        public task_d(int[][] Ma, int[] R){
            n = R.length;
            M = new int[n][n];
            for(int i = 0; i < n; ++i) {
                System.arraycopy(Ma[i], 0, M[i], 0, n);
            }
            this.R = R;
        }
        @Override
        public void run(){
            ThreadManager tm = new ThreadManager();
            long tn = System.currentTimeMillis() - TimeStart;
            System.out.println("время начала выполнения задачи D: " + tn);
            try {
                Thread.sleep(GlobalTime + ((int) (Math.random() * GlobalTime/2)));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int average = 0;
            for(int i = 0; i < n; ++i){average+=R[i];}
            average = average/n;
            for(int i = 0; i < n; ++i) { //f3:добавление ко всем элементам матрицы среднего значения массива
                for(int j = 0; j < n; ++j) {
                    M[i][j] += average;
                }
            }
            tn = System.currentTimeMillis() - TimeStart;
            System.out.println("результат выполнения задачи D:\nновая матрица:\n" + tm.toString(M, n) + "время окончания выполнения задачи D: " + tn);
            flag[2] = true;
            if (flag[0] && flag[1]){
                System.out.println("начинается инициация задачи E, ее инициировала задача D");
                Thread thr_e = new Thread(new task_e(task_b.M,task_c.M,M));
                thr_e.start();
                System.out.println("начинается инициация задачи F, ее инициировала задача D");
                Thread thr_f = new Thread(new task_f(task_b.M,task_c.M,M));
                thr_f.start();
            }
            tasks[3] = true;
        }
    }
    private static class task_e implements Runnable {
        public static int[][] Mb;
        public static int[][] Mc;
        public static int[][] Md;
        public int n;
        public task_e(int[][] Mb_,int[][] Mc_, int[][] Md_){
            n = Mb_.length;
            Mb = new int[n][n];
            Mc = new int[n][n];
            Md = new int[n][n];
            for(int i = 0; i < n; ++i) {
                System.arraycopy(Mb_[i], 0, Mb[i], 0, n);
                System.arraycopy(Mc_[i], 0, Mc[i], 0, n);
                System.arraycopy(Md_[i], 0, Md[i], 0, n);
            }
            flag[0]=false;
            flag[1]=false;
            flag[2]=false;
        }
        @Override
        public void run(){
            ThreadManager tm = new ThreadManager();
            long tn = System.currentTimeMillis() - TimeStart;
            System.out.println("время начала выполнения задачи E: " + tn);
            try {
                Thread.sleep(GlobalTime + ((int) (Math.random() * GlobalTime/2)));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for(int i = 0; i < n; ++i) { //f4:сложение матриц
                for(int j = 0; j < n; ++j) {
                    Mb[i][j] = Mb[i][j] + Mc[i][j] + Md[i][j];
                }
            }
            tn = System.currentTimeMillis() - TimeStart;
            System.out.println("результат выполнения задачи E:\nСуммирующая матрица:\n" + tm.toString(Mb, n) + "время окончания выполнения задачи E: " + tn);
            System.out.println("начинается инициация задачи G, ее инициировала задача E");
            Thread thr_g = new Thread(new task_g(Mb));
            thr_g.start();
            System.out.println("начинается инициация задачи H, ее инициировала задача E");
            Thread thr_h = new Thread(new task_h(Mb));
            thr_h.start();
            tasks[4] = true;
        }
    }
    private static class task_f implements Runnable {
        public static int[][] Mb;
        public static int[][] Mc;
        public static int[][] Md;
        public int n;
        public task_f(int[][] Mb_,int[][] Mc_, int[][] Md_){
            n = Mb_.length;
            Mb = new int[n][n];
            Mc = new int[n][n];
            Md = new int[n][n];
            for(int i = 0; i < n; ++i) {
                System.arraycopy(Mb_[i], 0, Mb[i], 0, n);
                System.arraycopy(Mc_[i], 0, Mc[i], 0, n);
                System.arraycopy(Md_[i], 0, Md[i], 0, n);
            }
        }
        @Override
        public void run(){
            ThreadManager tm = new ThreadManager();
            long tn = System.currentTimeMillis() - TimeStart;
            System.out.println("время начала выполнения задачи F: " + tn);
            try {
                Thread.sleep(GlobalTime*2 + ((int) (Math.random() * GlobalTime))); //удвоено среднее время выполнения (ожидания) задачи, что требовало условие варианта
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for(int i = 0; i < n; ++i) { //f5:сложение матриц функций f1 и f2 и вычитание из них удвоенной матрицы функции f3
                for(int j = 0; j < n; ++j) {
                    Mb[i][j] = Mb[i][j] + Mc[i][j] - (Md[i][j] * 2);
                }
            }
            tn = System.currentTimeMillis() - TimeStart;
            System.out.println("результат выполнения задачи F:\nСуммирующая матрица:\n" + tm.toString(Mb, n) + "время окончания выполнения задачи F: " + tn);
            flag[2] = true;
            if (flag[0] && flag[1]){
                System.out.println("начинается инициация задачи K, ее инициировала задача F");
                Thread thr_k = new Thread(new task_k(Mb,task_g.M,task_h.M));
                thr_k.start();
            }
            tasks[5] = true;
        }
    }
    private static class task_g implements Runnable {
        public static int[][] M;
        public int n;
        public task_g(int[][] Me){
            n = Me.length;
            M = new int[n][n];
            for(int i = 0; i < n; ++i) {
                System.arraycopy(Me[i], 0, M[i], 0, n);
            }
        }
        @Override
        public void run(){
            ThreadManager tm = new ThreadManager();
            long tn = System.currentTimeMillis() - TimeStart;
            System.out.println("время начала выполнения задачи G: " + tn);
            try {
                Thread.sleep(GlobalTime + ((int) (Math.random() * GlobalTime/2)));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for(int i = 0; i < n; ++i) { //f6:уменьшает элементы матрицы в 10 раз, отбрасывая остаток
                for(int j = 0; j < n; ++j) {
                    M[i][j] = M[i][j]/10;
                }
            }
            tn = System.currentTimeMillis() - TimeStart;
            System.out.println("результат выполнения задачи G:\nСуммирующая матрица:\n" + tm.toString(M, n) + "время окончания выполнения задачи G: " + tn);
            flag[0] = true;
            if (flag[1] && flag[2]){
                System.out.println("начинается инициация задачи K, ее инициировала задача G");
                Thread thr_k = new Thread(new task_k(task_f.Mb,M,task_h.M));
                thr_k.start();
            }
            tasks[6] = true;
        }
    }
    private static class task_h implements Runnable {
        public static int[][] M;
        public int n;
        public task_h(int[][] Me){
            n = Me.length;
            M = new int[n][n];
            for(int i = 0; i < n; ++i) {
                System.arraycopy(Me[i], 0, M[i], 0, n);
            }
        }
        @Override
        public void run(){
            ThreadManager tm = new ThreadManager();
            long tn = System.currentTimeMillis() - TimeStart;
            System.out.println("время начала выполнения задачи H: " + tn);
            try {
                Thread.sleep(GlobalTime + ((int) (Math.random() * GlobalTime/2)));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for(int i = 0; i < n; ++i) { //f6:записывает в матрицу остаток от деления элементов на 10
                for(int j = 0; j < n; ++j) {
                    M[i][j] = M[i][j]%10;
                }
            }
            tn = System.currentTimeMillis() - TimeStart;
            System.out.println("результат выполнения задачи H:\nСуммирующая матрица:\n" + tm.toString(M, n) + "время окончания выполнения задачи H: " + tn);
            flag[1] = true;
            if (flag[0] && flag[2]){
                System.out.println("начинается инициация задачи K, ее инициировала задача H");
                Thread thr_k = new Thread(new task_k(task_f.Mb,task_g.M,M));
                thr_k.start();
            }
            tasks[7] = true;
        }
    }
    private static class task_k implements Runnable {
        public static int[][] Mf;
        public static int[][] Mg;
        public static int[][] Mh;
        public int n;
        public task_k(int[][] Mf_,int[][] Mg_, int[][] Mh_){
            n = Mf_.length;
            Mf = new int[n][n];
            Mg = new int[n][n];
            Mh = new int[n][n];
            for(int i = 0; i < n; ++i) {
                System.arraycopy(Mf_[i], 0, Mf[i], 0, n);
                System.arraycopy(Mg_[i], 0, Mg[i], 0, n);
                System.arraycopy(Mh_[i], 0, Mh[i], 0, n);
            }
        }
        @Override
        public void run(){
            ThreadManager tm = new ThreadManager();
            long tn = System.currentTimeMillis() - TimeStart;
            System.out.println("время начала выполнения задачи K: " + tn);
            try {
                Thread.sleep(GlobalTime + ((int) (Math.random() * GlobalTime/2)));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for(int i = 0; i < n; ++i) { //f7:сложение матриц функций
                for(int j = 0; j < n; ++j) {
                    Mf[i][j] = Mf[i][j] + Mg[i][j] +Mh[i][j];
                }
            }
            tn = System.currentTimeMillis() - TimeStart;
            System.out.println("результат выполнения задачи K:\nСуммирующая матрица:\n" + tm.toString(Mf, n) + "время окончания выполнения задачи K: " + tn);
            tasks[8] = true;
        }
    }
public final String toString(int[] R, int n) { //создание строк для вывода массивов
    StringBuilder build = new StringBuilder();
    for(int j = 0; j < n; ++j) {build.append(R[j]).append(" ");}
    return build.toString();
}
public final String toString(int[][] M, int n) { //создание строк для вывода матриц
    StringBuilder build = new StringBuilder();
    for(int i = 0; i < n; ++i) {
        for(int j = 0; j < n; ++j) {build.append(M[i][j]).append(" ");}
        build.append("\n");
    }
    return build.toString();
}

}
