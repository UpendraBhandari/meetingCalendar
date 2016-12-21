package org.marketlogic.meetingCalender

import org.joda.time._
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import java.util.Arrays
import java.io.File
import java.util.Scanner;


class ValidationLogic{
  
  var submissionEntries  = scala.collection.mutable.ArrayBuffer[meetSubmit]()
  var submissionrequest = scala.collection.mutable.Map[LocalDateTime, Array[String]]()
  var validEntries = scala.collection.mutable.ArrayBuffer[meetSubmit]()
  
  def outOfOfficeTiming(wrkhrs: Array[String]) : (scala.collection.mutable.ArrayBuffer[meetSubmit], scala.collection.mutable.Map[LocalDateTime,Array[String]]) = {
    
    //this.submissionEntries = submissionEntries
    // validation 1 - logic to remove meeting entries outside office hours 
    for (entry <- 0 to submissionEntries.length-1){
       
			 var startTime = Array(submissionEntries(entry).meetStartDateTime.getHourOfDay) ++ Array(submissionEntries(entry).meetStartDateTime.getMinuteOfHour)
       var endTime = Array(submissionEntries(entry).meetEndDateTime.getHourOfDay) ++ Array(submissionEntries(entry).meetEndDateTime.getMinuteOfHour)
       
           
       if ((startTime(0)>=wrkhrs(0).splitAt(2)._1.toInt && startTime(1)>=wrkhrs(0).splitAt(2)._2.toInt) &&
       (endTime(0)<=wrkhrs(1).splitAt(2)._1.toInt && endTime(1)<=wrkhrs(1).splitAt(2)._2.toInt)){
         // println("Meeting within office hours")
         
       }else{
         // println("Meeting outside office hours")
         submissionrequest.remove(this.submissionEntries(entry).submitDate)
         //submissionEntries.remove(entry)
       }      
     }
     submissionEntries.clear()
     for (validEntry<-submissionrequest){
       validEntry._2(0)
       var entry = scala.collection.mutable.ArrayBuffer(meetSubmit(new LocalDateTime(validEntry._1), validEntry._2(0).toString, new LocalDateTime(validEntry._2(1)), new LocalDateTime(validEntry._2(2))))
       this.submissionEntries ++= entry
     }
     
    
    return (submissionEntries, submissionrequest)
  }
  
  def overlapTiming() = {
    
    for (entry <- submissionEntries){
       
       if(validEntries.isEmpty){
         // println("First Entry")
         validEntries ++= scala.collection.mutable.ArrayBuffer(entry)
         
       }else{
         // println("Next Entry")
         var tempStartDate = entry.meetStartDateTime
         var tempEndDate = entry.meetEndDateTime
         var found = 0
         
         for (incomingEntry <- 0 to validEntries.length-1){
            
            // println("Comparing Incoming entry against existing entry")
            
           // Logic to check whether 2 entries overlap
            if (((validEntries(incomingEntry).meetStartDateTime.isAfter(tempStartDate) || validEntries(incomingEntry).meetStartDateTime.isEqual(tempStartDate)) && 
                ((validEntries(incomingEntry).meetStartDateTime.isBefore(tempEndDate)) || validEntries(incomingEntry).meetStartDateTime.isBefore(tempEndDate))) ||
              ((validEntries(incomingEntry).meetEndDateTime.isAfter(tempStartDate) || (validEntries(incomingEntry).meetEndDateTime.isAfter(tempStartDate)))
              && ((validEntries(incomingEntry).meetEndDateTime.isBefore(tempEndDate) || validEntries(incomingEntry).meetEndDateTime.isBefore(tempEndDate)))))
            {
              // incoming meeting time overlaps over existing meeting time. Hence check submission dates.
              
              // if incoming submit date is older than then existing submit date
              if (entry.submitDate.isBefore(validEntries(incomingEntry).submitDate)){
                
                // println("Incoming entry is older than existing entry")
                validEntries(incomingEntry) = entry
                submissionrequest.remove(entry.submitDate)
                               
              }else{                
                // println("Ignoring incoming entry, since submission date is newer") 
                submissionrequest.remove(entry.submitDate)
              }  
              found=1
            }           
         }
         if (found==0){
              // println("Entry not overlapping, inserting")
              validEntries ++= scala.collection.mutable.ArrayBuffer(entry)
         }
         // println("Entry validation complete..awaiting next entry")            
         }    
        // println("-----------------------------------------------")
     } 
     submissionEntries.clear()
     for (validEntry<-submissionrequest){
       validEntry._2(0)
       var entry = scala.collection.mutable.ArrayBuffer(meetSubmit(new LocalDateTime(validEntry._1), validEntry._2(0).toString, new LocalDateTime(validEntry._2(1)), new LocalDateTime(validEntry._2(2))))
       submissionEntries ++= entry
     }
    
    
    
  }
  
}