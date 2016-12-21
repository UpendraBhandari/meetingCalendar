package org.marketlogic.meetingCalender

import org.joda.time._
import org.joda.time.format.DateTimeFormat
import java.util.Arrays
import java.io.File
import java.util.Scanner;
import scala.collection.mutable._

class DisplayLogic extends ValidationLogic {
  
  var timeList = List[java.util.Date]()
  var sortedTimeListMap = List[Map[java.util.Date,Array[String]]]()
  
  def display(submissionEntries: scala.collection.mutable.ArrayBuffer[meetSubmit]) = {
    
     val tempoutput = submissionEntries.toList.map { data => Map(data.meetStartDateTime.toDate()->Array(data.meetStartDateTime.getHourOfDay.toString+":"+data.meetStartDateTime.getMinuteOfHour.toString,
         data.meetEndDateTime.getHourOfDay.toString+":"+data.meetEndDateTime.getMinuteOfHour.toString,
         data.empID    
     )) }
     
     // this can be optimized by extending the comparator
     for (entry <- tempoutput){
       timeList ++= entry.keys
     }
     timeList = timeList.sortBy(_.getTime) // sort by meeting date, List has the capacity to sort based on date/time List[java.util.Date]
     
     // reorder the entries based on meeting start date
     var tmptimeList = List[Map[java.util.Date,Array[String]]]()
     for (entry <- timeList){
       
       for (meeting <- tempoutput){         
         if (meeting.keys.exists { x => x.equals(entry) }){
           tmptimeList ++= List(meeting)
         }         
       }      
     }
     
     // group entries by Date, scala advantage
     var sortedTimeListMap = tmptimeList.flatMap(x=>x).groupBy(x=>new LocalDateTime(x._1).getYear+"-"+new LocalDateTime(x._1).getMonthOfYear+"-"+new LocalDateTime(x._1).getDayOfMonth)
     
     // Final Display
     for(finaloutput <- sortedTimeListMap){
       val meetingDate = finaloutput._1
       println(meetingDate)
       for (entries <- finaloutput._2){
         val meetStartTime = entries._2(0)
         val meetEndTime = entries._2(1)
         val empID = entries._2(2)         
         println(meetStartTime +" "+ meetEndTime +" "+  empID)         
       }
     }
     
     
    
    
    
  }
  
  
}