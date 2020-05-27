$(document).ready(function(){
	countP();
	countRDV();
	countD();
	notif();
	patient();
	rdv();
	dos();
	
	function dos()
	{
		$.post('DossierLoad.php',function(data){
			$('.dos_table').html(data);
		});
	}
	setInterval(dos,1000);
	
	function rdv()
	{
		$.post('loadrdv.php',function(data){
			$('.rdv_table').html(data);
		});
	}
	setInterval(rdv,1000);
	
	function patient()
	{
		$.post('loadClients.php',function(data){
			$('.patients_table').html(data);
		});
	}
	setInterval(patient,1000);
	
	function notif()
	{
		$.post('notif.php',function(data){
			$('.title').html(data);
		});
	}
	setInterval(notif,1000);
	
	function countP()
	{
		$.post('countP.php',function(data){
			$('.notif_pat').html(data);
		});
	}
	setInterval(countP,1000);
	
	function countRDV()
	{
		$.post('countRDV.php',function(data){
			$('.notif_rdv').html(data);
		});
	}
	setInterval(countRDV,1000);
	
	function countD()
	{
		$.post('countD.php',function(data){
			$('.notif_doss').html(data);
		});
	}
	setInterval(countD,1000);
	
});