package za.co.empirestate.botspost.preamped;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by George_Kapoya on 16/01/23.
 */
public class History implements List<History> {

    public String hId,hAmount,hRev,hTranDateTime,meterNumber;

    //default constructor
    public History() {
    }

    public History(String hId, String hTranDateTime, String meterNumber, String hRev, String hAmount) {
        this.hId = hId;
        this.hTranDateTime = hTranDateTime;
        this.meterNumber = meterNumber;
        this.hRev = hRev;
        this.hAmount = hAmount;
    }

    public String gethId() {
        return hId;
    }

    public void sethId(String hId) {
        this.hId = hId;
    }

    public String gethAmount() {
        return hAmount;
    }

    public void sethAmount(String hAmount) {
        this.hAmount = hAmount;
    }

    public String gethRev() {
        return hRev;
    }

    public void sethRev(String hRev) {
        this.hRev = hRev;
    }

    public String getMeterNumber() {
        return meterNumber;
    }

    public void setMeterNumber(String meterNumber) {
        this.meterNumber = meterNumber;
    }

    public String gethTranDateTime() {
        return hTranDateTime;
    }

    public void sethTranDateTime(String hTranDateTime) {
        this.hTranDateTime = hTranDateTime;
    }

    //implement list
    @Override
    public void add(int i, History history) {

    }

    @Override
    public boolean add(History history) {
        return false;
    }

    @Override
    public boolean addAll(int i, Collection<? extends History> collection) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends History> collection) {
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
    public History get(int i) {
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
    public Iterator<History> iterator() {
        return null;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<History> listIterator() {
        return null;
    }

    @NonNull
    @Override
    public ListIterator<History> listIterator(int i) {
        return null;
    }

    @Override
    public History remove(int i) {
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
    public History set(int i, History history) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @NonNull
    @Override
    public List<History> subList(int i, int i1) {
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



}
