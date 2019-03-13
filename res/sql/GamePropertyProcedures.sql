drop procedure if exists property_create;
drop procedure if exists property_update;
drop procedure if exists player_property;
drop procedure if exists available_property;
drop procedure if exists position_property;
drop procedure if exists property_clean;

delimiter $$
create procedure property_create(
    in g_id int,
    in prop_id int
  )
  begin
    insert into gameproperty(game_id, property_id) values (g_id, prop_id);

    select gameproperty.*, property.position, property.price, property.categorycolor
      from gameproperty join property p
        on gameproperty.property_id = p.property_id
          where prop_id = property_id;
  commit;

end $$
delimiter ;


delimiter $$
create procedure property_update(
    in prop_id int,
    in g_id int,
    in pawn bit,
    in u_id int
  )
  begin
   update gameproperty set pawned = pawn
      where property_id = prop_id and game_id = g_id;

   update gameproperty set user_id = u_id
      where property_id = prop_id and game_id = g_id;

end $$
delimiter ;


delimiter $$
create procedure property_clean(
  in g_id int
)
begin
  delete from gameproperty where game_id = g_id;
commit;

end $$
delimiter ;


/*
delimiter $$
create procedure available_property(
)
begin
  select gameproperty.*, property.position, property.price, property.categorycolor
    from gameproperty join property p
      on gameproperty.property_id = p.property_id
        where user_id is null;
  commit;

end $$
delimiter ;



delimiter $$
create procedure player_property(
in u_id int
)
begin
  select gameproperty.*, property.position, property.price, property.categorycolor
    from gameproperty join property p
      on gameproperty.property_id = p.property_id
        where user_id = u_id;
  commit;

end $$
delimiter ;


delimiter $$
create procedure position_property(
  in pos int
)
begin
  select gameproperty.*, property.position, property.price, property.categorycolor
    from gameproperty join property p
      on gameproperty.property_id = p.property_id
        where position = pos;
  commit;

end $$
delimiter ;
*/
