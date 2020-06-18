package tada;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
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
        BeanUtils.copyProperties(this, drivingCreated);
        drivingCreated.publishAfterCommit();


        DrivingCanceled drivingCanceled = new DrivingCanceled();
        BeanUtils.copyProperties(this, drivingCanceled);
        drivingCanceled.publishAfterCommit();


        DrivingFinished drivingFinished = new DrivingFinished();
        BeanUtils.copyProperties(this, drivingFinished);
        drivingFinished.publishAfterCommit();


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
