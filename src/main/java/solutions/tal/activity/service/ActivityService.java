package solutions.tal.activity.service;

import solutions.tal.activity.model.Activity;
import solutions.tal.activity.model.ActivityFileRecord;

public interface ActivityService {

    Activity process(ActivityFileRecord record);
}
