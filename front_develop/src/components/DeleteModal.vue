<div class="modal">
    <div class="custom-modal-content">
        <h3>길드 탈퇴 처리를 하시겠습니까?</h3>
        <div class="form-group col-md-12" style="display: flex; margin-top: 20px;">
            <div class="col-md-3" style="margin-top: 3px; text-align: right;">
                <span style="font-size: 16px;">탈퇴 사유</span>
            </div>
            <div class="col-md-9">
                <input style="width: 80%; padding-right: 10px;" id="reason">
            </div>
        </div>
        <div class="form-group col-md-12" style="display: flex;">
            <div class="col-md-3" style="margin-top: 3px; text-align: right;">
                <span style="font-size: 16px;">승인자</span>
            </div>
            <div class="col-md-9">
                <select class="adminList" id="deleteAdmin">
                </select>
            </div>
        </div>
        <div>
            <button class="modal-delete success">확인</button>
            <button class="modal-delete-cancel error">취소</button>
        </div>
    </div>
</div>

<script>

</script>

<style>
.modal { 
    position:fixed; width:100%; height:100%; background: rgba(0,0,0,0.8); top:0%; left:0%;
}

.custom_modal_content {
    width:400px; height:300px;
    background:#fff; border-radius:10px;
    position:relative; top:30%; left:50%;
    margin-top:-50px; margin-left:-200px;
    text-align:center;
    box-sizing:border-box; padding:74px 0;
    line-height:23px;
}
</style>