package com.eveb.saasops.batch.game.n2.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class N2BetLogs {

    private List<Result> result;

    @Data
    public static class Result {

        private List<Gameinfo> gameinfo;

        @Data
        public static class Gameinfo {
            private List<Game> game;

            @Data
            public static class Game {
                private String code;
                private List<Deal> deal;

                @Data
                public static class Deal {
                    private Date enddate;
                    private String code;
                    private Long id;
                    private String status;
                    private Date startdate;
                    private List<Dealdetails> dealdetails;
                    private List<Clientbets> betinfo;
                    private List<result> results;

                    @Data
                    public static class result {
                        private String result;
                    }

                    @Data
                    public static class Dealdetails {
                        private List<Dealdetail> dealdetail;

                        @Data
                        private static class Dealdetail {
                            private String side;
                            private String type;
                            private String value;
                        }
                    }

                    @Data
                    public static class Clientbets {
                        private List<Clientbet> clientbet;

                        @Data
                        public static class Clientbet {
                            private String login;
                            private BigDecimal bet_amount;
                            private BigDecimal payout_amount;
                            private BigDecimal handle;
                            private BigDecimal hold;
                            private List<String> betdetail;
                            @Data
                            public static class betdetail {
                                private String betdetail;
                            }

                        }
                    }

                }
            }
        }
    }


}

