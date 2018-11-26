package com.tks.gwa.utils;

import com.tks.gwa.constant.AppConstant;
import com.tks.gwa.dto.LogSchedule;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class FileUtil {

    public static void logSchedule(String description, float cycle, String date) {
        File f = null;
        FileWriter fw = null;
        PrintWriter pw = null;

        try {
            int id = getLastIDFromFile();
            id += 1;

            f = new File(AppConstant.LOG_FILE_SCHEDULE);
            if (!f.exists()) {
                f.createNewFile();
            }
            fw = new FileWriter(f, true);
            pw = new PrintWriter(fw);


            if (id == 1) {
                pw.print(id + ";" + description + ";" + cycle + ";" + date);
                pw.println("");
            } else {
                pw.println(id + ";" + description + ";" + cycle + ";" + date);
            }
            System.out.println("Append to log schedule: " + id + ";" + description + ";" + cycle + ";" + date);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pw != null) {
                    pw.close();
                }
                if (fw != null) {
                    fw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static int getLastIDFromFile() {

        File f = null;
        FileReader fr = null;
        BufferedReader br = null;

        int id = 0;

        try {
            f = new File(AppConstant.LOG_FILE_SCHEDULE);
            if (!f.exists()) {
                return id;
            }

            fr = new FileReader(f);
            br = new BufferedReader(fr);
            String details;

            while ((details = br.readLine()) != null) {
                StringTokenizer stk = new StringTokenizer(details, ";");
                id = Integer.parseInt(stk.nextToken());
            }

            System.out.println("The last id of log schedule file: " + id);

            return id;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (NoSuchElementException e) {
            System.out.println("There is no data in log schedule file");
            return 0;
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (fr != null) {
                    fr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return 0;
    }

    public static List<LogSchedule> getLogScheduleFromFile() {
        List<LogSchedule> listLogs = new ArrayList<LogSchedule>();
        File f = null;
        FileReader fr = null;
        BufferedReader br = null;

        try {
            f = new File(AppConstant.LOG_FILE_SCHEDULE);
            if (!f.exists()) {
                return listLogs;
            }

            fr = new FileReader(f);
            br = new BufferedReader(fr);
            String details;

            while ((details = br.readLine()) != null) {
                StringTokenizer stk = new StringTokenizer(details, ";");
                int id = Integer.parseInt(stk.nextToken());
                String description = stk.nextToken();
                float cycle = Float.parseFloat(stk.nextToken());
                String date = stk.nextToken();

                LogSchedule logSchedule = new LogSchedule(id, description, cycle, date);
                listLogs.add(logSchedule);
            }
            System.out.println("Finish loading from log schedule file with result: " + listLogs + " and size: " + listLogs.size());

            return listLogs;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (fr != null) {
                    fr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return listLogs;
    }
}
