package com.pao.laboratory03.bonus.enumeratii;

public enum Status {
    TODO{
        @Override
        public boolean canTransitionTo(Status next){
            if(next == IN_PROGRESS || next ==CANCELLED)
                return true;
            return false;
        }
    },
    IN_PROGRESS{
        @Override
        public boolean canTransitionTo(Status next) {
            if (next == DONE || next == CANCELLED)
                return true;
            return false;
        }
    },
    DONE{
        @Override
        public boolean canTransitionTo(Status next){
            return false;
        }
    },
    CANCELLED{
        @Override
        public boolean canTransitionTo(Status next){
            return false;
        }
    };

    public abstract boolean canTransitionTo(Status next);
}
