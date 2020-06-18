package tada;

import javax.persistence.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeTypeUtils;
import tada.config.kafka.KafkaProcessor;

import java.util.List;

@Entity
@Table(name="Driving_table")
public class Driving {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long drivingId;
    private String starting;
    private String destination;
    private String drivingStatus;
    private Integer charge;
    private Long callId;

    @PostPersist
    public void onPostPersist(){
        DrivingCreated drivingCreated = new DrivingCreated();
        drivingCreated.setDrivingId(this.getDrivingId());
        drivingCreated.setStarting((this.getStarting()));
        drivingCreated.setDestination(this.getDestination());
        drivingCreated.setDrivingStatus("Running");
        drivingCreated.setCharge(this.getCharge());
        drivingCreated.setCallId(this.getCallId());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;

        try {
            json = objectMapper.writeValueAsString(drivingCreated);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON format exception", e);
        }

        KafkaProcessor processor = Application.applicationContext.getBean(KafkaProcessor.class);
        MessageChannel outputChannel = processor.outboundTopic();

        outputChannel.send(MessageBuilder
                .withPayload(json)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build());
    }

    @PostUpdate
    public void onPostUpdate() {
        if ("Finished".equals(this.getDrivingStatus())) {
            DrivingFinished drivingFinished = new DrivingFinished();
            drivingFinished.setDrivingId(this.getDrivingId());
            drivingFinished.setStarting((this.getStarting()));
            drivingFinished.setDestination(this.getDestination());
            drivingFinished.setDrivingStatus("Finished");
            drivingFinished.setCharge(this.getCharge());
            drivingFinished.setCallId(this.getCallId());

            ObjectMapper objectMapper = new ObjectMapper();
            String json = null;

            try {
                json = objectMapper.writeValueAsString(drivingFinished);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("JSON format exception", e);
            }

            KafkaProcessor processor = Application.applicationContext.getBean(KafkaProcessor.class);
            MessageChannel outputChannel = processor.outboundTopic();

            outputChannel.send(MessageBuilder
                    .withPayload(json)
                    .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                    .build());
        } else {
            DrivingCanceled drivingCanceled = new DrivingCanceled();
            drivingCanceled.setDrivingId(this.getDrivingId());
            drivingCanceled.setStarting((this.getStarting()));
            drivingCanceled.setDestination(this.getDestination());
            drivingCanceled.setDrivingStatus("Canceled");
            drivingCanceled.setCharge(this.getCharge());
            drivingCanceled.setCallId(this.getCallId());

            ObjectMapper objectMapper = new ObjectMapper();
            String json = null;

            try {
                json = objectMapper.writeValueAsString(drivingCanceled);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("JSON format exception", e);
            }

            KafkaProcessor processor = Application.applicationContext.getBean(KafkaProcessor.class);
            MessageChannel outputChannel = processor.outboundTopic();

            outputChannel.send(MessageBuilder
                    .withPayload(json)
                    .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                    .build());
        }

    }


    public Long getDrivingId() {
        return drivingId;
    }

    public void setDrivingId(Long drivingId) {
        this.drivingId = drivingId;
    }
    public String getStarting() {
        return starting;
    }

    public void setStarting(String starting) {
        this.starting = starting;
    }
    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
    public String getDrivingStatus() {
        return drivingStatus;
    }

    public void setDrivingStatus(String drivingStatus) {
        this.drivingStatus = drivingStatus;
    }
    public Integer getCharge() {
        return charge;
    }

    public void setCharge(Integer charge) {
        this.charge = charge;
    }
    public Long getCallId() {
        return callId;
    }

    public void setCallId(Long callId) {
        this.callId = callId;
    }




}
