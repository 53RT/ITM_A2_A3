  $(document).ready(function(){
       $('li img').on('click',function(){
           var src = $(this).attr('src');
           
           var src = "media/md/b17barb026.jpg.hist.png.thumb.png";
           var fileName = src.slice(8);
           var number = fileName.indexOf(".");
           fileName = fileName.slice(0,number+4);
           var dir = "media/img"
           src = dir.concat(fileName);
           
           var img = '<img src="' + src + '" class="img-responsive"/>';
           $('#myModal').modal();
           $('#myModal').on('shown.bs.modal', function(){
               $('#myModal .modal-body').html(img);
           });
           $('#myModal').on('hidden.bs.modal', function(){
               $('#myModal .modal-body').html('');
           });
      });  
