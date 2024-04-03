package org.qyliuhuo;

import java.util.*;

public class RHashMap<K,V> implements Map<K,V> {
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; //初始容量为15

    static final int MAXIMUM_CAPACITY = 1 << 30; //最大容量

    static final float DEFAULT_LOAD_FACTOR = 0.75f; //默认负载因子

    private int capacity;

    private float loadFactor;

    private Node<K,V> [] table;

    private int [] nodeCount;

    private int size = 0;

    private Set<K> keySet;

    private Set<Entry<K, V>> entrySet;

    private Collection<V> valueCollection;
    public RHashMap(){
        this.capacity = DEFAULT_INITIAL_CAPACITY;
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        table = (Node<K, V>[]) new Node[capacity];
        nodeCount = new int[capacity];
        keySet = new HashSet<>();
        entrySet = new HashSet<>();
        valueCollection = new LinkedList<>();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size()==0;
    }

    @Override
    public boolean containsKey(Object key) {
        int hash = calculateHash((K) key);
        int index = hash&(capacity-1);
        Node<K,V> head = table[index];
        while(head!=null){
            if(head.getHash()==hash){
                return true;
            }
            head=head.getNext();
        }
        return false;
        //return keySet.contains(key);
    }

    @Override
    public boolean containsValue(Object value) {
        for(int i=0;i<capacity;i++){
            Node<K,V> n = table[i];
            while(n!=null){
                if(n.getValue()==value)
                    return true;
                n=n.getNext();
            }
        }
        return false;
    }

    public K getRandomkey(){
        Node<K,V> selectedNode = null;
        if(size==0) return null;
        int tableSize = table.length;
        Random random =  new Random();
        while(selectedNode==null){
            int selectedIndex = random.nextInt(size);
            if(nodeCount[selectedIndex]==0) continue;
            int selectedNodeIndex = random.nextInt(nodeCount[selectedIndex]);
            selectedNode = table[selectedNodeIndex];
            for(int i=0;i<selectedNodeIndex;i++){
                selectedNode=selectedNode.getNext();
            }
        }
        return selectedNode.getKey();
    }
    @Override
    public V get(Object key) {
        int hash = calculateHash((K) key);
        int index = hash&(capacity-1);
        Node<K,V> head = table[index];
        while(head!=null){
            if(head.getHash()==hash){
                return head.getValue();
            }
            head=head.getNext();
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        putNode(key,value);
        if(size>capacity*loadFactor && (capacity<<1)<MAXIMUM_CAPACITY) resize();
        return value;
    }

    @Override
    public V remove(Object key) {
        int hash = calculateHash((K) key);
        int index = hash&(capacity-1);
        Node<K,V> head = table[index];
        if(head==null) return null;
        if(head.getHash()==hash){
            table[index]=head.getNext();
            nodeCount[index]--;
            --size;
            return head.getValue();
        }
        else{
            Node<K,V> pre =head;
            head = head.getNext();
            while(head!=null){
                if(head.getHash()==hash){
                    pre.setNext(head.getNext());//del
                    nodeCount[index]--;
                    --size;
                    return head.getValue();
                }
                pre = head;
                head=head.getNext();
            }
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for(Entry<K, V> node : entrySet){
            put(node.getKey(), node.getValue());
        }
    }

    @Override
    public void clear() {
        this.capacity = DEFAULT_INITIAL_CAPACITY;
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        table = (Node<K, V>[]) new Node[capacity];
        nodeCount = new int[capacity];
        keySet = new HashSet<>();
    }

    @Override
    public Set<K> keySet() {
        return keySet;
    }

    @Override
    public Collection<V> values() {
        return valueCollection;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return entrySet;
    }

    private void resize(){
        int newCapacity = capacity<<1;
        Node<K,V> [] newTable = new Node[newCapacity];
        int [] newNodeCount = new int[newCapacity];
        for(int i=0;i<capacity;i++){
            Node<K,V> n = table[i];
            while(n!=null){
                int newIndex = n.getHash()&(newCapacity-1);
                Node<K,V>  head = newTable[newIndex];
                if(head==null){
                    newTable[newIndex]=n;
                }
                else{
                    while(head.getNext()!=null){
                        head=head.getNext();
                    }
                    head.setNext(n);
                }
                newNodeCount[newIndex]++;
                Node<K,V> temp = n.getNext();
                n.setNext(null);
                n = temp;
            }
        }
        capacity = newCapacity; //扩容为两倍
        table = newTable;
        nodeCount = newNodeCount;
        //System.out.println("resize！！！！！！！");
    }

    private void putNode(K key,V value){
        int hash = calculateHash(key);
        Node<K,V> newNode = new Node<>(key,value,hash);
        int index = (capacity-1)&hash;
        Node<K,V> head = table[index];
        if(head==null){
            table[index]=newNode;
            nodeCount[index]++;
            ++size;
        }
        else{
            while(head!=null){
                if(head.getHash()==hash){
                    head.setValue(value);
                    return;
                }
                head=head.getNext();
            }
            head.setNext(newNode);
            nodeCount[index]++;
            ++size;
        }
        keySet.add(key);
        entrySet.add(newNode);
        valueCollection.add(value);
    }
    private int calculateHash(K key){
        if(key==null)
            return 0;
        int h =key.hashCode();
        return h^(h>>16);
    }
}

class Node<K,V> implements Map.Entry<K,V>{
    private final int hash;

    private final K key;

    private V value;

    private Node<K,V> next;

    public Node(K key,V value,int hash) {
        this.key = key;
        this.value = value;
        this.hash = hash;
    }

    public void setNext(Node<K,V> nextNode){
        next = nextNode;
    }
    public Node<K,V> getNext(){
        return next;
    }
    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        return null;
    }

    public int getHash(){
        return hash;
    }

}

