CREATE TABLE public.user_account (
	id uuid NOT NULL,
	email varchar(50) NOT NULL,
	"password" varchar(50) NOT NULL,
	login varchar(20) NOT NULL,
	CONSTRAINT uk_user_account_username UNIQUE (login),
	CONSTRAINT pk_user_account PRIMARY KEY (id)
);