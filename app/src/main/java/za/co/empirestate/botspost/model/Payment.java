package za.co.empirestate.botspost.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Payment
  implements Parcelable
{
  public static final Parcelable.Creator<Payment> CREATOR = new Parcelable.Creator()
  {
    public Payment createFromParcel(Parcel paramAnonymousParcel)
    {
      return new Payment(paramAnonymousParcel);
    }

    public Payment[] newArray(int paramAnonymousInt)
    {
      return new Payment[paramAnonymousInt];
    }
  };
  public String amount;
  public String cardHolderName;
  public String cardHolderSurname;
  public String cardNumber;
  public String cardType;
  public String cvv;
  public String expMonth;
  public String expYear;
  public String meterNumber;

  public Payment(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }

  public Payment(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String paramString8, String paramString9)
  {
    this.cardHolderName = paramString1;
    this.cardHolderSurname = paramString2;
    this.cardType = paramString3;
    this.cardNumber = paramString4;
    this.expMonth = paramString5;
    this.expYear = paramString6;
    this.meterNumber = paramString7;
    this.amount = paramString8;
    this.cvv = paramString9;
  }

  private void readFromParcel(Parcel paramParcel)
  {
    this.cardHolderName = paramParcel.readString();
    this.cardHolderSurname = paramParcel.readString();
    this.cardType = paramParcel.readString();
    this.cardNumber = paramParcel.readString();
    this.expMonth = paramParcel.readString();
    this.expYear = paramParcel.readString();
    this.meterNumber = paramParcel.readString();
    this.amount = paramParcel.readString();
    this.cvv = paramParcel.readString();
  }

  public int describeContents()
  {
    return 0;
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(this.cardHolderName);
    paramParcel.writeString(this.cardHolderSurname);
    paramParcel.writeString(this.cardType);
    paramParcel.writeString(this.cardNumber);
    paramParcel.writeString(this.expMonth);
    paramParcel.writeString(this.expYear);
    paramParcel.writeString(this.meterNumber);
    paramParcel.writeString(this.amount);
    paramParcel.writeString(this.cvv);
  }
}