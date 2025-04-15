package com.trip.IronBird_Server.plan.application.service;


import com.trip.IronBird_Server.plan.adapter.dto.ScheduleDto;

public interface ScheduleService {

    public ScheduleDto createSchedule(ScheduleDto scheduleDto, Long planId);
}
