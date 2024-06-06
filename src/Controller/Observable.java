package Controller;

public interface Observable {
    public void addObserver(Observer o);
    public void removeObserver(Observer o);
    public Object get();
}
