package za.co.empirestate.botspost.preamped;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by es_air13_1 on 16/03/07.
 */
public class PoboxObj implements List<PoboxObj> {


    public String HolderType,Name,PaidUntil,Size,StartDate,Status,LastPaidUntil,RenewalAmount,PenaltyAmount,NextPaidUntil,TransactionHandle;
    public  String  postBoxId,groupId;
    public PoboxObj() {
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getHolderType() {
        return HolderType;
    }

    public void setHolderType(String holderType) {
        HolderType = holderType;
    }

    public String getPostBoxId() {
        return postBoxId;
    }

    public void setPostBoxId(String postBoxId) {
        this.postBoxId = postBoxId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPaidUntil() {
        return PaidUntil;
    }

    public void setPaidUntil(String paidUntil) {
        PaidUntil = paidUntil;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getLastPaidUntil() {
        return LastPaidUntil;
    }

    public void setLastPaidUntil(String lastPaidUntil) {
        LastPaidUntil = lastPaidUntil;
    }

    public String getRenewalAmount() {
        return RenewalAmount;
    }

    public void setRenewalAmount(String renewalAmount) {
        RenewalAmount = renewalAmount;
    }

    public String getPenaltyAmount() {
        return PenaltyAmount;
    }

    public void setPenaltyAmount(String penaltyAmount) {
        PenaltyAmount = penaltyAmount;
    }

    public String getNextPaidUntil() {
        return NextPaidUntil;
    }

    public void setNextPaidUntil(String nextPaidUntil) {
        NextPaidUntil = nextPaidUntil;
    }

    public String getTransactionHandle() {
        return TransactionHandle;
    }

    public void setTransactionHandle(String transactionHandle) {
        TransactionHandle = transactionHandle;
    }

    @Override
    public void add(int i, PoboxObj poboxObj) {

    }

    @Override
    public boolean add(PoboxObj poboxObj) {
        return false;
    }

    @Override
    public boolean addAll(int i, Collection<? extends PoboxObj> collection) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends PoboxObj> collection) {
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
    public PoboxObj get(int i) {
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
    public Iterator<PoboxObj> iterator() {
        return null;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<PoboxObj> listIterator() {
        return null;
    }

    @NonNull
    @Override
    public ListIterator<PoboxObj> listIterator(int i) {
        return null;
    }

    @Override
    public PoboxObj remove(int i) {
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
    public PoboxObj set(int i, PoboxObj poboxObj) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @NonNull
    @Override
    public List<PoboxObj> subList(int i, int i1) {
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
