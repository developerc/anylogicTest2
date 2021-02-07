package main;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class HandleTicketJson {
    public String ticketsJson = null;
    public String percentil90;

    HandleTicketJson(String path){
        List<Character> chars = new ArrayList<>();
        //читаем содержимое файла
        StringBuffer sb = new StringBuffer("");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(new File(path)));
            int c;
            while ((c = reader.read()) != -1) {
                chars.add((char) c);
            }
            reader.close();
        } catch (IOException e) {
            chars = null;
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (chars != null) {
            for (char myChar : chars) {
                sb.append(myChar);
            }
            ticketsJson = sb.toString();
        }
    }

    public  Map getObjFromJson(){
        Type listType = new TypeToken<Map<String, ArrayList<Ticket>>>(){}.getType();
        Map<String, ArrayList<Ticket>> map = new Gson().fromJson(ticketsJson, listType);
        return map;
    }

    public String getAverageTime(ArrayList<Ticket> ticketArrayList){
        List<Long> durationList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.MM.dd HH:mm");
        LocalDateTime departureDate;
        LocalDateTime arrivalDate;
        Duration duration;
        long diff;
        long sumDurations = 0;
        long averageTime;
        double n;
        for (Ticket ticket : ticketArrayList){
            departureDate = LocalDateTime.parse(improveDateTime(ticket.getDeparture_date(), ticket.getDeparture_time()), formatter);
            arrivalDate = LocalDateTime.parse(improveDateTime(ticket.getArrival_date(), ticket.getArrival_time()),formatter);
            duration = Duration.between(departureDate, arrivalDate);
            diff = Math.abs(duration.toMinutes());
            sumDurations = sumDurations + diff;
            durationList.add(diff);
        }
        averageTime = sumDurations / durationList.size();    //вычисляем среднее время

        Collections.sort(durationList);
        n = (90.0/100.0)*(durationList.size() - 1)+ 1;  //вычисляем ранг
        percentil90 = String.valueOf(durationList.get(((int) n) - 1));  //вычисляем процентиль

        return String.valueOf(averageTime);
    }

    private String improveDateTime(String dat, String tim){
        String improveTime;
        String [] arrTim = tim.split(":");
        if (arrTim[0].length() < 2){
            improveTime = '0' + arrTim[0] + ":" + arrTim[1];
        } else {
            improveTime = tim;
        }
        return dat + " " + improveTime;
    }

}
