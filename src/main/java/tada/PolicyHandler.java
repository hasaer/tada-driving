package tada;

import tada.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{

    @Autowired
    DrivingRepository drivingRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void onEvent(@Payload String message) { }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCalled_CreateDriving(@Payload Called called){

        if(called.isMe()){
            //System.out.println("##### listener CreateDriving : " + called.toJson());
            Driving driving = new Driving();
            driving.setStarting((called.getStarting()));
            driving.setDestination(called.getDestination());
            driving.setDrivingStatus("Running");
            driving.setCharge(called.getCharge());
            driving.setCallId(called.getCallId());
            drivingRepository.save(driving);
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCallCanceled_CancelDriving(@Payload CallCanceled callCanceled){

        if(callCanceled.isMe()){
            //System.out.println("##### listener CancelDriving : " + callCanceled.toJson());
            Driving driving = drivingRepository.findByDrivingId(callCanceled.getDrivingId());
            driving.setDrivingId(callCanceled.getDrivingId());
            driving.setStarting((callCanceled.getStarting()));
            driving.setDestination(callCanceled.getDestination());
            driving.setDrivingStatus("Canceled");
            driving.setCharge(callCanceled.getCharge());
            driving.setCallId(callCanceled.getCallId());
            drivingRepository.save(driving);
        }
    }

}
