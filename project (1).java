import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

class project {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        MyMap<String, Rectangle> parishs = new MyMap<>();
        KDNode node=null;
        KDTree tree=new KDTree(node);
        while (input.hasNext()){
            String string= input.nextLine();
            if(string.equals("addN")){
                System.out.println("name:");
                String name=input.next();
                double[][] carr=new double[4][2];
                System.out.println("coordinates:");
                for (int i = 0; i < 4; i++) {
                   /* String s=input.next();
                    System.out.println(s);
                    String[] c=s.split(" ");
                    double[] carri=new double[2];
                    carri[0]=Double.parseDouble(c[0]);
                    carri[1]=Double.parseDouble(c[1]);
                    carr[i]=carri;*/
                    String s1=input.next();
                    String s2=input.next();
                    double[] carri=new double[2];
                    carri[0]=Double.parseDouble(s1);
                    carri[1]=Double.parseDouble(s2);
                    carr[i]=carri;
                }
                Rectangle rectangle=new Rectangle();
                rectangle.bounds=carr;
                parishs.put(name,rectangle);
            }else if(string.equals("addB")){
                System.out.println("bname:");
                String name =input.next();
                System.out.println("coordinates:");
                String s1=input.next();
                String s2=input.next();
                double[] carr=new double[2];
                carr[0]=Double.parseDouble(s1);
                carr[1]=Double.parseDouble(s2);
                if(tree.searchbool(carr)){
                    System.out.println("There is another bank at this point!");
                }else{
                 Bank bank=new Bank(carr);
                 bank.bname=name;
                 KDNode kdNode=new KDNode(bank);
                 tree.add(kdNode);
                }
            }else if(string.equals("addBr")){
                System.out.println("bname:");
                String bname =input.next();
                System.out.println("brname:");
                String brname =input.next();
                System.out.println("coordinates:");
                String s1=input.next();
                String s2=input.next();
                double[] carr=new double[2];
                carr[0]=Double.parseDouble(s1);
                carr[1]=Double.parseDouble(s2);
                if(tree.searchbool(carr)){
                    System.out.println("There is another bank at this point!");
                }else{
                    Bank bank=new Bank(carr);
                    bank.bname=bname;
                    bank.brname=brname;
                    KDNode kdNode=new KDNode(bank);
                    tree.add(kdNode);
                    //search bank and add it into branchs
                    tree.searchbyname(bname).value.branchs.add(bank);
                }
            }else if(string.equals("delBr")){
                System.out.println("coordinates:");
                String s1=input.next();
                String s2=input.next();
                double[] carr=new double[2];
                carr[0]=Double.parseDouble(s1);
                carr[1]=Double.parseDouble(s2);
                KDNode temp=new KDNode(new Bank(carr));
                tree.delete(temp);
            }else if(string.equals("listB")){
                System.out.println("pname:");
                String pname=input.next();
                Rectangle rectangle=parishs.get(pname);
                tree.inorder(rectangle);
            }else if(string.equals("listBrs")){
                System.out.println("bname:");
                String bname =input.next();
                Bank ourbank=tree.searchbyname(bname).value;
                for (int i = 0; i < ourbank.branchs.size(); i++) {
                    System.out.println("("+ourbank.branchs.get(i).coordinates[0]+","+ourbank.branchs.get(i).coordinates[1]+")");
                }
            }else if(string.equals("nearB")){
                System.out.println("coordinates:");
                String s1=input.next();
                String s2=input.next();
                double[] carr=new double[2];
                carr[0]=Double.parseDouble(s1);
                carr[1]=Double.parseDouble(s2);
                Bank tempbank=new Bank(carr);
                Bank nearest=tree.nearest(tempbank).value;
                System.out.println("nearest bank name is:"+nearest.bname);
                if(nearest.brname!=null)
                    System.out.println("and branch name is:"+nearest.brname);
                System.out.println("point: ("+nearest.coordinates[0]+","+nearest.coordinates[1]+")");
            }else if(string.equals("nearBr")){
                System.out.println("coordinates:");
                String s1=input.next();
                String s2=input.next();
                double[] carr=new double[2];
                carr[0]=Double.parseDouble(s1);
                carr[1]=Double.parseDouble(s2);
                Bank tempbank=new Bank(carr);
                System.out.println("bname:");
                String bname =input.next();
                Bank ourbank=tree.searchbyname(bname).value;
                KDNode brsNode=null;
                KDTree brstree=new KDTree(brsNode);
                for (int i = 0; i < ourbank.branchs.size(); i++) {
                    brstree.add(new KDNode(ourbank.branchs.get(i)));
                }
                Bank nearest=brstree.nearest(tempbank).value;
                System.out.println("nearest bank name is:"+nearest.bname);
                if(nearest.brname!=null)
                    System.out.println("and branch name is:"+nearest.brname);
                System.out.println("point: ("+nearest.coordinates[0]+","+nearest.coordinates[1]+")");
            }else if(string.equals("availB")){
                System.out.println("R:");
                int R= input.nextInt();
                System.out.println("coordinates:");
                String s1=input.next();
                String s2=input.next();
                double[] carr=new double[2];
                carr[0]=Double.parseDouble(s1);
                carr[1]=Double.parseDouble(s2);
                Circle circle =new Circle();
                circle.rdious=R;
                circle.centre=carr;
                tree.inorder2(circle);
            }

        }

    }

    static class Entry<K, V> {
        final K key;
        V value;
        Entry<K, V> next;


        public Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

    }
    public static class MyMap<K, V> {
        Entry<K, V>[] buckets;
        static final int INITIAL_CAPACITY = 40;

       int size = 0;

        public MyMap() {
            this(INITIAL_CAPACITY);
        }

        public MyMap(int capacity) {
            this.buckets = new Entry[capacity];
        }

        long getHashCode(K key) {

            String keyString = key.toString();
            return keyString.hashCode();
        }

        public void put(K key, V value) {
            Entry<K, V> entry = new Entry<>(key, value, null);

            int bucket = (int) (getHashCode(key) % buckets.length);

            Entry<K, V> existing = buckets[bucket];
            if (existing == null) {
                buckets[bucket] = entry;
                size++;
            } else {
                while (existing.next != null) {
                    if (existing.key.equals(key)) {
                        existing.value = value;
                        return;
                    }
                    existing = existing.next;
                }

                if (existing.key.equals(key)) {
                    existing.value = value;
                } else {
                    existing.next = entry;
                    size++;
                }
            }
        }

        public V get(K key) {
            Entry<K, V> bucket = buckets[(int) (getHashCode(key) % buckets.length)];

            while (bucket != null) {
                if (bucket.key.equals(key)) {
                    return bucket.value;
                }
                bucket = bucket.next;
            }
            return null;
        }
    }

    static class Rectangle{
        double[][] bounds;
         boolean ishere(double[] point){
             if(point[0]<=bounds[3][0] && point[0]>=bounds[0][0] && point[1]<=bounds[1][1] && point[1]>=bounds[0][1])
               return true;
             else
               return false;
         }
    }
    static class Circle{
        double[] centre;
        int rdious;
        double dist(double[] coordinates1,double[] coordinates2){
            double sum = 0;
            for (int i = 0; i < 2; i++) {
                sum+=ABC((coordinates1[i]-coordinates2[i])*(coordinates1[i]-coordinates2[i]));
            }
            return sum;
        }
        double ABC(double num){
            if(num<0)
                return (-1*num);
            else
                return num;
        }
        boolean ishere(double[] point){
            if(dist(centre,point)<=(rdious*rdious))
                return true;
            else
                return false;
        }
    }

   /* public class MyArrayList {

        private Object[] myStore;
        private int actSize = 0;

        public MyArrayList(){
            myStore = new Object[10];
        }

        public Object get(int index){
            if(index < actSize){
                return myStore[index];
            } else {
                throw new ArrayIndexOutOfBoundsException();
            }
        }

        public void add(Object obj){
            if(myStore.length-actSize <= 5){
                increaseListSize();
            }
            myStore[actSize++] = obj;
        }

        public Object remove(int index){
            if(index < actSize){
                Object obj = myStore[index];
                myStore[index] = null;
                int tmp = index;
                while(tmp < actSize){
                    myStore[tmp] = myStore[tmp+1];
                    myStore[tmp+1] = null;
                    tmp++;
                }
                actSize--;
                return obj;
            } else {
                throw new ArrayIndexOutOfBoundsException();
            }

        }

        public int size(){
            return actSize;
        }

        private void increaseListSize(){
            myStore = Arrays.copyOf(myStore, myStore.length*2);
            System.out.println("\nNew length: "+myStore.length);
        }}*/

    public static class MyArrayList<E extends Object> {

        int initialCapacity = 5;
        int currentSize;
        Object[] myArrayList = {},
        temp = {};

        int currentIndex = 0;

        public MyArrayList() {
            myArrayList = new Object[initialCapacity]; }

        public MyArrayList(int size) {
            myArrayList = new Object[size];
        }

        public void add(Object anyObj) {
            //add element directy
            myArrayList[currentIndex] = anyObj;
            currentSize = myArrayList.length;
            currentIndex++;
            if (currentIndex == currentSize) {
                createDoubleSizedArray(currentSize);
            }
        }
        int size(){
            return currentIndex;
        }
        private void createDoubleSizedArray(int currentSize) {
            temp = myArrayList.clone();
            myArrayList = new MyArrayList[2 * currentSize];
            System.arraycopy(temp, 0, myArrayList, 0, currentSize);

        }

        public E get(int index) throws RuntimeException{
            if (index >= currentSize){
                throw new IndexOutOfBoundsException();
            }
            return (E) myArrayList[index];
        }

        void delete(Object object) {
            if (currentIndex == 0) {
                System.out.println("empty array!");
                return;
            }
            currentIndex--;
        }
    }

   /* static class Bank{
        String bname;
        MyArrayList<Bank> branchs;
        double[] coordinates;
        String brname;//if it is branch

        public Bank(double[] c){
            this.coordinates=c;
            branchs=new MyArrayList<Bank>();
        }

        double get(int d){
            return coordinates[d%coordinates.length];
        }

        int size(){
            return coordinates.length;
        }
    }*/

    static class KDNode{
        KDNode left;
        KDNode right;
        double[] data;
        Bank value;
        int dims=2;

        public KDNode(Bank bank) {
            this.value = bank;
            this.data=bank.coordinates;
        }

        public void add(KDNode node,int k){
            if(node.data[k] < data[k]){
                if(left==null)
                    left=node;
                else
                    left.add(node,(k+1)%dims);
            }
            else{
                if(right==null)
                    right=node;
                else
                    right.add(node,(k+1)%dims);
            }
        }
        public void add(KDNode node){
            this.add(node,0);
        }

        public KDNode(double[] x){
            this.value=new Bank(x);
        }
    }

    static class KDTree{
        KDNode root;
        int DIM=2;

        public KDTree(KDNode root) {
            this.root=root;
        }

        public KDTree(MyArrayList<double[]> banks){
           this.DIM=2;
           this.root=new KDNode(banks.get(0));

            for (int i = 1,banksnum=banks.size(); i < banksnum; ++i) {
              double[] bank=banks.get(i);
              KDNode node=new KDNode(bank);
              this.root.add(node);
            }
        }

        void inorder(Rectangle rectangle){
            inorder(root,rectangle);
        }
        void inorder(KDNode r,Rectangle rec){
            if (r != null){
                inorder(r.left,rec);
                if(rec.ishere(r.data)) {
                    System.out.println("bname:"+r.value.bname+" coordinates:("+r.data[0]+","+r.data[1]+")");
                    if(r.value.brname!=null)
                        System.out.println("brname:"+r.value.brname);
                }
                inorder(r.right,rec);
            }
        }

        void inorder2(Circle circle){
            inorder2(root,circle);
        }
        void inorder2(KDNode r,Circle circle){
            if (r != null){
                inorder2(r.left,circle);
                if(circle.ishere(r.data)) {
                    System.out.println("bname:"+r.value.bname+"coordinates:("+r.data[0]+","+r.data[1]+")");
                    if(r.value.brname!=null)
                        System.out.println("brname:"+r.value.brname);
                }
                inorder2(r.right,circle);
            }
        }

        public boolean isEmpty(){
            return root == null;
        }


        public void add(KDNode kdnode){
           if(root==null)
               this.root=kdnode;
           else
               this.root.add(kdnode);
        }

        public void add(double[] bank){
            KDNode node=new KDNode(bank);
            if(root==null)
                this.root=node;
            else
                this.root.add(node);
        }

        public KDNode search(double []data){
            return search(data,root,0);
        }

        private KDNode search(double[] x,KDNode t,int cd){
            KDNode found=null;
            if(t==null){
                return null;
            }
            else {
                if(x[cd]==t.data[cd]){
                    if(x[0]==t.data[0] && x[1]==t.data[1]) {
                        System.out.println("founded");
                        return t;
                    }
                }else if(x[cd]<t.data[cd]){
                    found = search(x,t.left,(cd+1)%DIM);
                }else if(x[cd]>t.data[cd]){
                    found = search(x,t.right,(cd+1)%DIM);
                }
                return found;
            }
        }

        public boolean searchbool(double []data){
            return searchbool(data,root,0);
        }

        private boolean searchbool(double []x,KDNode t,int cd){
            boolean found=false;
            if(t==null){
                return false;
            }
            else {
                if(x[cd]==t.data[cd]){
                    if(x[0]==t.data[0] && x[1]==t.data[1]) {
                        System.out.println("founded");
                        return true;
                    }
                }else if(x[cd]<t.data[cd]){
                    found = searchbool(x,t.left,(cd+1)%DIM);
                }else if(x[cd]>t.data[cd]){
                    found = searchbool(x,t.right,(cd+1)%DIM);
                }
                return found;
            }
        }

        public KDNode searchbyname(String itsname){
            return searchbyname(itsname,root,0);
        }

        private KDNode searchbyname(String itsname,KDNode t,int cd){
            KDNode found=null;
            if(t==null){
                return null;
            }
            else {
                if(itsname.equals(t.value.bname)){
                        return t;
                }
                else{
                    found=searchbyname(itsname,t.left,(cd+1)%DIM);
                    found = searchbyname(itsname,t.right,(cd+1)%DIM);
                }
                return found;
            }
        }

        KDNode nearest(KDNode root,Bank t,int depth){
            if(this.root==null)
                return null;
            KDNode nex=null;
            KDNode other=null;
            if(t.get(depth) < this.root.value.get(depth)){
               nex=root.left;
               other=root.right;
            }else{
                nex=root.right;
                other=root.left;
            }

            KDNode temp=nearest(nex,t,depth+1);
            KDNode best=closest(temp,root,t);
            double radius=dist(t, best.value);
            double dist=t.get(depth)-this.root.value.get(depth);
            if(radius >= dist*dist){
                temp=nearest(other,t,depth+1);
                best=closest(temp,best,t);
            }
            return best;
        }

        KDNode nearest(Bank t){
            return nearest(this.root,t,0);
        }

        KDNode closest(KDNode X0,KDNode X1,Bank t){
            if(X0==null)
                return X1;
            if(X1==null)
                return X0;
            double dis1=dist(X0.value,t);
            double dis2=dist(X1.value,t);
            if(dis1<dis2)
                return X0;
            else
                return X1;
        }

        double dist(Bank b0,Bank b1){
            double sum = 0;
            for (int i = 0; i < 2; i++) {
               sum+=ABC((b0.coordinates[i]-b1.coordinates[i])*(b0.coordinates[i]-b1.coordinates[i]));
            }
            return sum;
        }

        public boolean delete(KDNode t) {
            if (root == null)
                return false;
            boolean isExist =false;
            KDNode ournode=this.search(t.data);
            if(ournode!=null)
                isExist=true;
            if (isExist == false) {
                System.out.println("this bank doesn't exist!");
                return false;
            }
            else if(ournode.value.brname==null){
                System.out.println("this is the main bank!");
                return false;
            }
            root = delete(root, t, 0);
            return true;
        }

        KDNode delete(KDNode curRoot, KDNode t, int curDim) {
            if (curRoot == null)
                return null;
            curDim = curDim % DIM;
            if (t.equals(curRoot)) {
                if (curRoot.right != null) {
                    KDNode rightMin = findMinimum(curRoot.right, curDim, curDim + 1);
                    curRoot.data = rightMin.data;
                    curRoot.right = delete(curRoot.right, rightMin, curDim + 1);
                } else if (curRoot.left != null) {
                    KDNode leftMin = findMinimum(curRoot.left, curDim, curDim + 1);
                    curRoot.data = leftMin.data;
                    curRoot.right = delete(curRoot.left, leftMin, curDim + 1);
                    curRoot.left = null;
                } else {
                    return null;
                }
                if (curRoot.left != null && curRoot.left.data[curDim] >= curRoot.data[curDim] ||
                        curRoot.right != null && curRoot.right.data[curDim] < curRoot.data[curDim])
                    System.err.println("SOMETHING WRONG");
                return curRoot;
            }
            if (t.data[curDim] < curRoot.data[curDim]) {
                curRoot.left = delete(curRoot.left, t, curDim + 1);
            } else {
                curRoot.right = delete(curRoot.right, t, curDim + 1);
            }
            return curRoot;
        }

        KDNode findMinimum(KDNode root, int tDim, int curDim) {
            if (root == null)
                return null;
            if (tDim == curDim) {
                if (root.left == null)
                    return root;
                else
                  return findMinimum(root.left, tDim, (curDim + 1) % DIM);
            }
            KDNode rightMin = findMinimum(root.right, tDim, (curDim + 1) % DIM);
            KDNode leftMin = findMinimum(root.left, tDim, (curDim + 1) % DIM);
            KDNode r = root;
            if (rightMin != null && rightMin.data[tDim] < r.data[tDim])
                r = rightMin;
            if (leftMin != null && leftMin.data[tDim] < r.data[tDim])
                r = leftMin;
            return r;
        }

        double ABC(double num){
            if(num<0)
                return (-1*num);
            else
                return num;
        }
    }
}
class Bank{
    String bname;
    project.MyArrayList<Bank> branchs;
    double[] coordinates;
    String brname;//if it is branch

    public Bank(double[] c){
        this.coordinates=c;
        branchs=new project.MyArrayList<Bank>();
    }

    double get(int d){
        return coordinates[d%coordinates.length];
    }

    int size(){
        return coordinates.length;
    }
}
