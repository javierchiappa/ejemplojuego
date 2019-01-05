;tareas a hacer: colision particula-escenario produce explosion
;
;


;VARIBALES
Global gravityacc#=0.00,startspeed#=2
Global camera
Global rx#,ry#,remx,remy
Global bulletentity
Global angle#,direction#

Type bullet
	Field x#,y#,z#
	Field ax#,ay#,az#
	Field e
	Field speed#
	Field gravity#
	
End Type



type_nave=1
type_escenario=2
type_disparo=3


;FIN VARIABLES




Graphics3D 800,600
SetBuffer BackBuffer()

Include "keyconstants.bb"
Include "particlemanager.bb"
;camera
camera_pivot = CreatePivot(nave)
EntityType camera_pivot,type_nave

camera = CreateCamera(camera_pivot)
;BALAS
bulletentity=LoadSprite("SPARK.BMP",0,nave)

EntityType bulletentity,type_disparo;colision

HideEntity bulletentity
ScaleEntity bulletentity,0.2,0.2,0.2
;LUCES
light=CreateLight()
;Escudo
escudo=CreateSphere(8,Camera_pivot)
EntityAlpha escudo,0.2
ScaleEntity escudo,1.5,1.5,1.5
HideEntity escudo


;MESHES
escena=LoadMesh ("escena1.3ds")
EntityType escena,type_escenario;colision escenario

nave=LoadMesh("nave2.3ds")
EntityType nave,type_nave;colision nave

PositionEntity nave,0,5,0
PositionEntity camera,0,-5,10


PM_AddEmitter(6,EntityX(camera_pivot),EntityY(camera_pivot),EntityZ(camera_pivot),1,nave)

While Not KeyHit(1) ;COMIENZA BUCLE


PointEntity camera,camera_pivot

PositionEntity nave,EntityX (camera_pivot), EntityY (camera_pivot),EntityZ (camera_pivot)
PointEntity camera,nave
	;mueve la nave
    If KeyDown(200) Then jy=jy-1;up
	If KeyDown(208) Then jy=jy+1;down

	If KeyDown(203) Then jx=jx-1;left
	If KeyDown(205) Then jx=jx+1;right


	tpitch#=tpitch+jy
	dpitch#=(tpitch-rpitch#)*.2
	rpitch=rpitch+dpitch

	tturn#=tturn+jx
	dturn#=(tturn-rturn#)*.2
	rturn=rturn+dturn

	tbank#=dturn*45
	dbank#=(tbank-rbank#)*.08
	rbank=rbank+dbank

	RotateEntity camera_pivot,-rpitch,-rturn,rbank;poner -
    RotateEntity nave,-rpitch,-rturn,rbank

    jy=0 
    jx=0     
	MoveEntity camera_pivot,0,0,-.25
    ;CONTROLA LAS BALAS
   If KeyDown(57) Then 
createbullet(startspeed#,EntityX (camera_pivot),EntityY (camera_pivot),EntityZ (camera_pivot),EntityPitch(CAMERA_PIVOT),EntityYaw(camera_pivot),EntityRoll(camera_pivot))
   
    EndIf
     
    ;controla colisiones

     Collisions type_nave,type_escenario,2,2  
      
    ;muestra el escudo
    col=EntityCollided(camera_pivot,type_escenario);si colisiona

	If col;
	 ;PM_AddEmitter(0,EntityX (camera_pivot),EntityY (camera_pivot),EntityZ (camera_pivot),0,0)
     ShowEntity escudo
    Else
     HideEntity escudo
    EndIf


       


     col2=EntityCollided(bulletentity,type_escenario);si colision particula con escenario col2=1

     If col2=True ;si colision particula con escenario
      PM_AddEmitter(0,EntityX (bulletentity),EntityY (bulletentity),EntityZ (bulletentity),0,0)
      Text 10,10,"colisiono"
     EndIf





If (KeyDown(key_Z)) Then

MoveEntity camera_pivot,0,0,2
End If

updatebullets()
PM_Update(camera)


UpdateWorld
RenderWorld

Flip
Wend
End

;FUNCIONES
Function createbullet(speed#,x#,y#,z#,ax#,ay#,az#)

b.bullet=New bullet
b\speed#=speed#
b\x#=x#
b\y#=y#
b\z#=z#
b\ax#=ax#
b\ay#=ay#
b\az#=az#
b\e=CopyEntity(bulletentity)
PositionEntity b\e,b\x,b\y,b\z
RotateEntity b\e,b\ax,b\ay,b\az

End Function

Function updatebullets()

For b.bullet=Each bullet
 MoveEntity b\e,0,0,-b\speed#
 EntityType b\e,TYPE_disparo
 be=b\e
 TranslateEntity b\e,0,-b\gravity#,0
 b\gravity#=b\gravity#+gravityacc#
 
     

 If EntityDistance(camera,b\e)>100 Then FreeEntity b\e:Delete b

Next
End Function


