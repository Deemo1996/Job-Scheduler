# Job-Scheduler
ABC company is developing a new operating system. As a software developer in the company you are required to implement a job scheduler for this new operating system. It has been decided that when the processor becomes free, the scheduler will assign to it a job that has been run for the least amount of time so far. This job will run for the smaller of 5ms and the amount of remaining time it needs to complete. In case the job does not complete in 5ms it becomes a candidate for the next scheduling round. A job has the following fields:

jobID: Unique ID for each job.

executed_time: The amount of time for which the job has been scheduled so far.

total_time: Total time required to complete the job. So, the remaining time to complete is total_time- executed_time.
