if exists (select * from sysobjects where id = object_id('dbo.job_px_order') and sysstat & 0xf = 4)
    drop procedure dbo.job_px_order
GO

create procedure dbo.job_px_order
WITH ENCRYPTION
AS BEGIN
  declare @Err		int;
  declare @BreakPoint	int;
  declare @StartWork	int;
  declare @Msg		varchar(255);

  declare @serialId	int;
  declare @refSheetType	int;
  declare @refSheetId	Varchar(32);
  declare @PxSheetId    Varchar(64);
  declare @PurSheetId	varchar(32);

  SET NOCOUNT ON
  SET REMOTE_PROC_TRANSACTIONS ON
  SET XACT_ABORT ON			--分布式事务必须
  SET ANSI_NULLS ON			--分布式事务必须
  SET ANSI_WARNINGS ON			--分布式事务必须

  select @BreakPoint=100001;
  select @Msg = '',@Err=0, @StartWork=0;

  select @BreakPoint=100010;
  update px_order_log0 set [status] = '2'
  where [status] = '1';


  select @BreakPoint=100020;
  DECLARE Cur_px_order_log cursor local for 
	select SerialID,refSheetType,refSheetId from px_order_log where status = '2';
  OPEN Cur_px_order_log
  select @Err = @@error;
  if @Err != 0 goto ErrHandle;
  WHILE (1=1)
  BEGIN
    if @@Error != 0 goto ErrHandle;
	FETCH NEXT FROM Cur_px_order_log INTO @serialId,@refSheetType,@refSheetId
	if @@fetch_status !=0 break;

	begin tran;
	set @StartWork = 1;

	update px_order_log set lastUpdateDate = getdate()
	where SerialID = @serialId;

	if @refSheetType = 2001 begin  --创建订单
	  select @BreakPoint=100030;
	  select @PxSheetId = 'P' + rtrim(SheetID) from purchase
	  where sheetid = @refSheetId;

	  

	  if not exists (select 1 from v_px_order where sheetid = @PxSheetId and sheettype = 2001) begin
	    select @BreakPoint=100040;
	    insert into v_px_order (sheetid,sheettype,orderNo,shopNo,orderType,orderDate,deliveryDate,orderTitle,deliveryStatus)
	    select @refSheetId,2001,@PxSheetId,rtrim(a.shopid),'Z001',a.checkdate,a.checkdate + 1,rtrim(a.notes),-1
		from purchase a
		where a.sheetid = @refSheetId;

		select @Err = @@error;
        if @Err != 0 begin
		  if @StartWork = 1 rollback tran;
		  begin tran;
		  update px_order_log set msg = '写[v_px_order]数据失败',status = '99'
		  where SerialID = @serialId;
		  commit tran;
		end;

		select @BreakPoint=100045;
		insert into v_px_Order_Items (orderNo,goodscode,BarCode,GoodsId,num)
		select @PxSheetId, a.goodsid, a.BarCodeID, a.goodsid, a.qty + a.PresentQty
		from purchaseItem a
		where a.sheetid = @refSheetId;

		select @Err = @@error;
        if @Err != 0 begin
		  if @StartWork = 1 rollback tran;
		  begin tran;
		  update px_order_log set msg = '写[v_px_order_items]数据失败',status = '99'
		  where SerialID = @serialId;
		  commit tran;
		end;
	  end
	  else begin
	    update px_order_log set msg = '[v_px_order]查询重复批销订单',status = '99'
		where SerialID = @serialId;

		if @StartWork = 1
		  commit;

		CONTINUE;
	  end;
	end
	else if @refSheetType = 2002 begin
	  --验收更新
	  select @BreakPoint=100060;

	  select @PurSheetId = PurSheetID from receiptref where sheetid = @refSheetId;

	  if @PurSheetId is null begin
	    update px_order_log set msg = '[receiptref]查询不到相关订单号',status = '99'
		where SerialID = @serialId;

		if @StartWork = 1
		  commit;

		CONTINUE;
	  end;

	  set @PxSheetId = 'P' + rtrim(@PurSheetId);
	  if exists (select 1 from v_px_order where orderNo = @PxSheetId and sheettype = 2001) begin
	    select @BreakPoint=100070;
		
	    update v_px_order set deliveryStatus = -2, ReceiptSheetId = @RefSheetID
	    where orderno = @PxSheetId;

		select @Err = @@error;
        if @Err != 0 begin
		  if @StartWork = 1 rollback tran;
		  begin tran;
		  update px_order_log set msg = '[v_px_order]更新失败',status = '99'
		  where SerialID = @serialId;
		  commit tran;
		end;

		update v_px_Order_Items set ReceiptQty = b.qty + b.PresentQty, receiptdate = getdate()
		from v_px_Order_Items a
			left join ReceiptItem b on a.barcode = b.barcodeid and b.sheetid = @RefSheetID
		where orderno = @PxSheetId;

		select @Err = @@error;
		if @Err != 0 begin
		  if @StartWork = 1 rollback tran;
		  begin tran
		  update px_order_log set msg = '[v_px_order_items]更新失败',status = '99'
		  where SerialID = @serialId;
		  commit tran;
		end;
	  end
	  else begin
	    update px_order_log set msg = '[v_px_order]查询不到批销订单',status = '99'
		where SerialID = @serialId;

		if @StartWork = 1
		  commit tran;

		CONTINUE;
	  end;
	end;

	update px_order_log set status = '90'
		where SerialID = @serialId;

	if @StartWork = 1
	  commit;
  end;
  CLOSE Cur_px_order_log
  DEALLOCATE Cur_px_order_log


  --备份处理
  begin tran;
  set @StartWork = 1;

  insert into px_order_log(CreateDate,refSheetType,refSheetId,status,lastUpdateDate,msg)
  select CreateDate,refSheetType,refSheetId,'100',lastUpdateDate,msg from px_order_log0
  where status = '90';

  select @Err = @@error;
  if @Err != 0 begin
	if @StartWork = 1 rollback tran;
	begin tran;
	update px_order_log set msg = '写[px_order_log]数据失败',status = '99'
	where SerialID = @serialId;
	commit tran;  
  end;

  delete from px_order_log0
  where status = '90';
  select @Err = @@error;
  if @Err != 0 begin
	if @StartWork = 1 rollback tran;
	begin tran;
	update px_order_log set msg = '备份数据后，清理[px_order_log0]数据失败',status = '99'
	where SerialID = @serialId;
	commit tran;  
  end;

  if @StartWork = 1
	commit tran;

  return 0;

ErrHandle:
  if @@error <> 0 select @Err=@@error;
  if @@TRANCOUNT > 0 rollback transaction;
  raiserror('%s,断点=%d,Err=%d',16,1,@Msg,@BreakPoint,@Err);
  return -1;

End
Go


declare @ID int
select @ID=Max(ID) from Job_TimerPolicy

if @ID is null
  select @ID=1
else
  select @ID=@ID+1

if not exists(select 1 from Job_TimerPolicy where procSQL='job_px_order')
  Insert into Job_TimerPolicy(id,active,clock,clocktype,needTrans,lastActive,nextActive,procSQL,notes,MaxRetry)
	Values(@ID,1,'00:05:00',0,0,getdate(),getdate(),'job_px_order','批销订单处理',10);
go

