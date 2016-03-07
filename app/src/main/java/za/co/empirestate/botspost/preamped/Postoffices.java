package za.co.empirestate.botspost.preamped;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by es_air13_1 on 16/03/02.
 */
public class Postoffices implements List<Postoffices> {

    public  int id;
    public String PostOfficeName;
    public  String groupID;

    public Postoffices() {
    }

    public Postoffices(String postOfficeName, String groupID) {
        PostOfficeName = postOfficeName;
        this.groupID = groupID;
    }

    public String getPostOfficeName() {
        return PostOfficeName;
    }

    public void setPostOfficeName(String postOfficeName) {
        PostOfficeName = postOfficeName;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    @Override
    public void add(int i, Postoffices postoffices) {

    }

    @Override
    public boolean add(Postoffices postoffices) {
        return false;
    }

    @Override
    public boolean addAll(int i, Collection<? extends Postoffices> collection) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends Postoffices> collection) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return false;
    }

    @Override
    public Postoffices get(int i) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @NonNull
    @Override
    public Iterator<Postoffices> iterator() {
        return null;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<Postoffices> listIterator() {
        return null;
    }

    @NonNull
    @Override
    public ListIterator<Postoffices> listIterator(int i) {
        return null;
    }

    @Override
    public Postoffices remove(int i) {
        return null;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return false;
    }

    @Override
    public Postoffices set(int i, Postoffices postoffices) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @NonNull
    @Override
    public List<Postoffices> subList(int i, int i1) {
        return null;
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @NonNull
    @Override
    public <T> T[] toArray(T[] ts) {
        return null;
    }

    public long getId() {
        return id;
    }
}
