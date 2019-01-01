/**
 * Created by xiongyi on 2018/12/30.
 */
var $ = function(id){
    return document.getElementById(id);
}

$('plusNum').onclick = function(e){
    e = window.event || e;
    o = e.srcElement || e.target;
    var num = $('allNum').textContent;
    if(num > 0){
        num --;
        $('allNum').innerHTML = num;
    }else{
        alert("您没有购买任何商品");
    }
};

$('addNum').onclick = function(e){
    e = window.event || e;
    o = e.srcElement || e.target;
    var num = $('allNum').textContent;
    num ++;
    $('allNum').innerHTML = num;
};

var loading = new Loading();
var layer = new Layer();


$('add').onclick = function(e){
    var msg = "您确定要购买吗？";
    if (confirm(msg)==true){
        var ele = e.target;
        var id = ele && ele.dataset.id;
        var title = ele && ele.dataset.title;
        var price = ele && ele.dataset.price;
        var num = $('allNum').innerHTML;
        var productDetail = {'id':id,'price':price,'title':title,'num':num};
        alert(productDetail)
//         var name = 'products';
//         var productList1 = new Array;
//         var productList = util.getCookie(name);
//         if(productList == "" || productList == null){
//             productList1.push(productDetail);
//             util.setCookie(name,productList1);
//         }else if(util.findOne(productList,id)){
//             util.modifyTwo(productList,id,num);
//             util.setCookie(name,productList);
//         }else{
//             productList.push(productDetail);
//             util.setCookie(name,productList);
//         }
//         console.log(document.cookie);
// //		util.deleteCookie(name);
//         e == window.event || e;
//         layer.reset({
//             content:'纭鍔犲叆璐墿杞﹀悧锛�',
//             onconfirm:function(){
//                 layer.hide();
//                 loading.show();
//                 loading.result('娣诲姞璐墿杞︽垚鍔�');
//             }.bind(this)
//         }).show();
        return;  //你也可以在这里做其他的操作
    }
    return;
};