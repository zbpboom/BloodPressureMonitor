package com.zhanbp.bloodpressuremonitor;

import java.util.ArrayList;
import java.util.Collections;

public class Signal {

    public ArrayList[] findPeak(int[] data) {

        ArrayList[] peakpeak = new ArrayList[4]; //波峰值，波峰位置，波谷值，波谷位置
        int[] PeakAndTrough=new int[data.length];

        for(int j = 0; j < 4;  j++)
        {
            peakpeak[j] = new ArrayList<Integer>();
        }

        //需要三个不同的值进行比较，取lo,mid，hi分别为三值
        for (int lo=0,mid=1,hi=2;hi<data.length;hi++){
            //先令data[lo]不等于data[mid]
            while (mid<data.length && data[mid]==data[lo]){
                mid++;
            }

            hi=mid+1;

            //令data[hi]不等于data[mid]
            while (hi<data.length&&data[hi]==data[mid]){
                hi++;
            }

            if (hi>=data.length){
                break;
            }

            //检测是否为峰值
            if (data[mid]>data[lo] && data[mid]>data[hi]){
                PeakAndTrough[mid]=1;       //1代表波峰
            }else if(data[mid]<data[lo] && data[mid]<data[hi]){
                PeakAndTrough[mid]=-1;      //-1代表波谷
            }

            lo=mid;
            mid=hi;
        }

        //计算均值
        float ave=0;
        for (int i=0;i<data.length;i++){
            ave+=data[i];
        }
        ave/=data.length;

        //排除大于均值的波谷和小于均值的波峰
        for (int i=0; i<PeakAndTrough.length; i++){
            if ((PeakAndTrough[i]>0 && data[i]<ave) || (PeakAndTrough[i]<0 && data[i]>ave)){        //如果有bug就在ave上加减一个阈值
                PeakAndTrough[i]=0;
            }
        }

        //统计波峰数量
        for (int i=0;i<PeakAndTrough.length;){
            while (i<PeakAndTrough.length && PeakAndTrough[i]>=0){//寻找第一个波谷
                i++;
            }
            if (i>=PeakAndTrough.length){
                break;
            }
            peakpeak[0].add(data[i]);
            peakpeak[1].add(i);
            //peak[][]++;

            while (i<PeakAndTrough.length && PeakAndTrough[i]<=0){//寻找波峰
                i++;
            }
            if (i>=PeakAndTrough.length){
                break;
            }
            peakpeak[2].add(data[i]);
            peakpeak[3].add(i);
        }

        return peakpeak;

    }

    public int meanPPvalue (ArrayList[] PP){
        int mean = 0;
        int length = PP[0].size();

        for (int i=0; i < length-1; i++){
            mean += ((Integer) PP[2].get(i) - (Integer) PP[0].get(i))/(length-1);
        }

        return mean;
    }
    public int[] findmaxPP(ArrayList[] PP){
        int[] maxPP= new int[3];// max, index1, index2
        int indexOfindex;
        ArrayList PPvalue = new ArrayList();

        int length = PP[0].size();
        for(int i=0; i < length-1; i++){
            PPvalue.add((Integer) PP[2].get(i) - (Integer) PP[0].get(i));
        }
        maxPP[0] = (int) Collections.max(PPvalue);
        indexOfindex =PPvalue.indexOf(maxPP[0]);
        maxPP[1] = (int) PP[1].get(indexOfindex);
        maxPP[2] = (int) PP[1].get(indexOfindex + 1);

        return maxPP;
    }

    public float calcuKbp(ArrayList<Integer> ACvalue, int PPvalue, int Hindex, int Eindex ){
        float Pm=0,Pd,Kbp;
        for(int i=Hindex; i<Eindex; i++){
            Pm += (float)ACvalue.get(i)/(Eindex-Hindex);
        }
        Pd = (float) (ACvalue.get(Hindex)+ACvalue.get(Eindex))/2;
        Kbp = (Pm - Pd)/PPvalue;
        return Kbp;
    }
}
